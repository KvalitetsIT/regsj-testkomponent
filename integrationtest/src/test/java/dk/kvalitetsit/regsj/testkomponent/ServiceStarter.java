package dk.kvalitetsit.regsj.testkomponent;

import com.github.dockerjava.api.model.VolumesFrom;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;
import org.openapitools.model.Context;
import org.openapitools.model.ContextResponse;
import org.openapitools.model.HelloResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ServiceStarter {
    private static final Logger logger = LoggerFactory.getLogger(ServiceStarter.class);
    private static final Logger serviceLogger = LoggerFactory.getLogger("regsj-testkomponent");
    private static final Logger mysqlLogger = LoggerFactory.getLogger("mysql");
    private static final Logger mockServerLogger = LoggerFactory.getLogger("remoteServer");

    private Network dockerNetwork;
    private String jdbcUrl;
    private Integer remoteServerMappedPort;

    public static final String DB_USER = "testkomponentuser";;
    public static final String DB_PASSWORD = "secret1234";
    public static final String DB_NAME = "testkomponentdb";

    public void startServices() {
        dockerNetwork = Network.newNetwork();

        setupDatabaseContainer();
        startRemoteCallMockServer();
        // Do not cache thymeleaf templates when running from IDE.
        System.setProperty("spring.thymeleaf.cache", "false");

        System.setProperty("CONFIGURABLE_TEXT", "En tekst");
        System.setProperty("ENVIRONMENT", "DEV");
        System.setProperty("usercontext.header.name", "x-sessiondata");
        System.setProperty("userattributes.org.key", "Organisation");
        System.setProperty("REMOTE_ENDPOINT", "http://localhost:" + remoteServerMappedPort + "/unprotected");
        System.setProperty("REMOTE_ENDPOINT_PROTECTED", "http://localhost:" + remoteServerMappedPort + "/protected");
        System.setProperty("DO_SERVICE_CALL", "true");
        System.setProperty("JDBC.URL", jdbcUrl);
        System.setProperty("JDBC.USER", DB_USER);
        System.setProperty("JDBC.PASS", DB_PASSWORD);

        SpringApplication.run((VideoLinkHandlerApplication.class));
    }

    public GenericContainer startServicesInDocker() {
        dockerNetwork = Network.newNetwork();

        setupDatabaseContainer();
        startRemoteCallMockServer();

        var resourcesContainerName = "regsj-testkomponent-resources";
        var resourcesRunning = containerRunning(resourcesContainerName);
        logger.info("Resource container is running: " + resourcesRunning);

        GenericContainer service;

        // Start service
        if (resourcesRunning) {
            VolumesFrom volumesFrom = new VolumesFrom(resourcesContainerName);
            service = new GenericContainer<>("local/regsj-testkomponent-qa:dev")
                    .withCreateContainerCmdModifier(modifier -> modifier.withVolumesFrom(volumesFrom))
                    .withEnv("JVM_OPTS", "-javaagent:/jacoco/jacocoagent.jar=output=file,destfile=/jacoco-report/jacoco-it.exec,dumponexit=true,append=true -cp integrationtest.jar");
        } else {
            service = new GenericContainer<>("local/regsj-testkomponent-qa:dev")
                    .withFileSystemBind("/tmp", "/jacoco-report/")
                    .withEnv("JVM_OPTS", "-javaagent:/jacoco/jacocoagent.jar=output=file,destfile=/jacoco-report/jacoco-it.exec,dumponexit=true -cp integrationtest.jar");
        }

        service.withNetwork(dockerNetwork)
                .withNetworkAliases("regsj-testkomponent")

                .withEnv("LOG_LEVEL", "INFO")

                .withEnv("JDBC_URL", "jdbc:mysql://mysql:3306/" + DB_NAME)
                .withEnv("JDBC_USER", DB_USER)
                .withEnv("JDBC_PASS", DB_PASSWORD)

                .withEnv("spring.flyway.locations", "classpath:db/migration,filesystem:/app/sql")

                .withEnv("CONFIGURABLE_TEXT", "En tekst")
                .withEnv("ENVIRONMENT", "DEV")

                .withEnv("REMOTE_ENDPOINT", "http://remoteServer:1080/unprotected")
                .withEnv("REMOTE_ENDPOINT_PROTECTED", "http://remoteServer:1080/protected")

                .withEnv("DO_SERVICE_CALL", "true")

                .withEnv("usercontext.header.name", "x-sessiondata")
                .withEnv("userattributes.org.key", "Organisation")

//                .withEnv("JVM_OPTS", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000")

                .withExposedPorts(8081,8080)
                .waitingFor(Wait.forHttp("/actuator").forPort(8081).forStatusCode(200));
        service.start();
        attachLogger(serviceLogger, service);

        return service;
    }

    private MockServerContainer startRemoteCallMockServer() {
        MockServerContainer remoteCallService = new MockServerContainer(DockerImageName.parse("jamesdbloom/mockserver:mockserver-5.11.2"))
                .withNetworkAliases("remoteServer")
                .withNetwork(dockerNetwork);
        remoteCallService.start();
        attachLogger(mockServerLogger, remoteCallService);

        remoteServerMappedPort = remoteCallService.getServerPort();

        MockServerClient mockServerClient = new MockServerClient(remoteCallService.getContainerIpAddress(), remoteCallService.getMappedPort(1080));
        mockServerClient.when(HttpRequest.request().withMethod("GET").withPath("/unprotected"), Times.unlimited()).respond(getRemoteResponse());
        mockServerClient.when(HttpRequest.request().withMethod("GET").withPath("/protected"), Times.unlimited()).respond(getRemoteResponseProtected());

        return remoteCallService;
    }

    private HttpResponse getRemoteResponse() {
        var content = new HelloResponse();
        content.setHostname("localhost");
        content.setVersion("1.0.0");

        var responseBodyWithContentType = JsonBody.json(content);

        return new HttpResponse().withBody(responseBodyWithContentType).withHeader("Content-Type", "application/json");
    }

    private HttpResponse getRemoteResponseProtected() {
        var content = new ContextResponse();
        content.setContext(new ArrayList<>());
        Context context1 = new Context();
        context1.setAttributeName("k1");
        context1.setAttributeValue(Arrays.asList("v1", "v2"));
        content.getContext().add(context1);

        var responseBodyWithContentType = JsonBody.json(content);

        return new HttpResponse().withBody(responseBodyWithContentType).withHeader("Content-Type", "application/json");
    }


    private boolean containerRunning(String containerName) {
        return DockerClientFactory
                .instance()
                .client()
                .listContainersCmd()
                .withNameFilter(Collections.singleton(containerName))
                .exec()
                .size() != 0;
    }

    private void setupDatabaseContainer() {
        // Database server for Organisation.
        MySQLContainer mysql = (MySQLContainer) new MySQLContainer("mysql:5.7")
                .withDatabaseName(DB_NAME)
                .withUsername(DB_USER)
                .withPassword(DB_PASSWORD)
                .withNetwork(dockerNetwork)
                .withNetworkAliases("mysql");
        mysql.start();
        jdbcUrl = mysql.getJdbcUrl();
        attachLogger(mysqlLogger, mysql);
    }

    private void attachLogger(Logger logger, GenericContainer container) {
        ServiceStarter.logger.info("Attaching logger to container: " + container.getContainerInfo().getName());
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);
        container.followOutput(logConsumer);
    }
}
