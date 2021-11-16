package dk.kvalitetsit.hello.integrationtest;

import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.KithugsApi;
import org.openapitools.client.model.HelloRequest;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.*;

public class HelloIT extends AbstractIntegrationTest {

    private final KithugsApi helloApi;

    public HelloIT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());

        helloApi = new KithugsApi(apiClient);
    }

//    @Test
//    public void testCallService() throws ApiException {
//        var input = new HelloRequest();
//        input.setName("John Doe");
//
//        var result = helloApi.v1HelloPost(input);
//
//        assertNotNull(result);
//        assertEquals(input.getName(), result.getName());
//        assertNotNull(result.getNow());
//    }

    @Test
    public void testHtmlPage() {
        WebTarget webTarget = ClientBuilder
                .newClient()
                .target(getApiBasePath())
                .path("/html");

        var result = webTarget
                .request()
                .header("x-sessiondata", "ew0KICAgICJVc2VyQXR0cmlidXRlcyI6IHsNCiAgICAgICAgIlVzZXJSb2xlcyI6IFsNCiAgICAgICAgICAgICJwcm92aXNpb25lcnJvbGUiDQogICAgICAgIF0sDQogICAgICAgICJPcmdhbmlzYXRpb24iOiBbInNvbWVfb3JnIl0NCiAgICB9DQp9")
                .get(String.class);

        assertNotNull(result);
        assertTrue(result.contains("DEV"));
        assertTrue(result.contains("En tekst"));
        assertTrue(result.contains("[provisionerrole]"));
        assertTrue(result.contains("[some_org]"));
    }
}
