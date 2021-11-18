package dk.kvalitetsit.regsj.testkomponent.configuration;

import dk.kvalitetsit.logging.RequestIdGenerator;
import dk.kvalitetsit.regsj.testkomponent.remote.TestkomponentClient;
import dk.kvalitetsit.regsj.testkomponent.remote.TestkomponentClientImpl;
import dk.kvalitetsit.regsj.testkomponent.service.RestService;
import dk.kvalitetsit.regsj.testkomponent.service.RestServiceImpl;
import dk.kvalitetsit.regsj.testkomponent.service.HtmlService;
import dk.kvalitetsit.regsj.testkomponent.service.HtmlServiceImpl;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextInterceptor;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextServiceImpl;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebMvc
public class TestkomponentConfiguration implements WebMvcConfigurer {
    private Logger logger = LoggerFactory.getLogger(TestkomponentConfiguration.class);

    @Autowired
    private UserContextInterceptor userContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userContextInterceptor);
    }

    @Bean
    public TestkomponentClient testkomponentClient(@Value("${REMOTE_ENDPOINT}") String remoteEndpoint, RequestIdGenerator requestIdGenerator, UserContextService userContextService) {
        logger.info("Configuring TestkomponentClient with remote url {}.", remoteEndpoint);
        return new TestkomponentClientImpl(remoteEndpoint, requestIdGenerator, userContextService);
    }

    @Bean
    public UserContextInterceptor userContextInterceptor(UserContextService userContextService) {
        userContextInterceptor = new UserContextInterceptor(userContextService);
        return userContextInterceptor;
    }

    @Bean
    public RestService helloService(UserContextService userContextService) {
        return new RestServiceImpl(userContextService);
    }

    @Bean
    public HtmlService htmlService(VersionProvider versionProvider, @Value("${CONFIGURABLE_TEXT}") String configurableText, @Value("${ENVIRONMENT}") String environment, UserContextService userContextService, TestkomponentClient testkomponentClient, @Value("${DO_SERVICE_CALL}") boolean doServiceCall) {
        return new HtmlServiceImpl(versionProvider, configurableText, environment, userContextService, doServiceCall, testkomponentClient);
    }

    @Bean
    @RequestScope
    public UserContextService userContextService(HttpServletRequest request,
                                                 @Value("${usercontext.header.name}") String userContextHeaderName,
                                                 @Value("${userattributes.org.key}") String organisationKey) {
        return new UserContextServiceImpl(request, userContextHeaderName, organisationKey);
    }
}
