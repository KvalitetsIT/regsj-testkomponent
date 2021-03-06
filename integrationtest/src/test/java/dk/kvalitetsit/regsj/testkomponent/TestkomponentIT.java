package dk.kvalitetsit.regsj.testkomponent;

import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TestKomponentApi;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.*;

public class TestkomponentIT extends AbstractIntegrationTest {
    private final TestKomponentApi helloApi;

    public TestkomponentIT() {
        var apiClient = new ApiClient();
        apiClient.setBasePath(getApiBasePath());
        apiClient.addDefaultHeader("x-sessiondata", "ew0KICAgICJVc2VyQXR0cmlidXRlcyI6IHsNCiAgICAgICAgIlVzZXJSb2xlcyI6IFsNCiAgICAgICAgICAgICJwcm92aXNpb25lcnJvbGUiDQogICAgICAgIF0sDQogICAgICAgICJPcmdhbmlzYXRpb24iOiBbInNvbWVfb3JnIl0NCiAgICB9DQp9");

        helloApi = new TestKomponentApi(apiClient);
    }

    @Test
    public void testCallRestService() throws ApiException {
        var result = helloApi.restV1HelloGet();

        assertNotNull(result);
        assertNotNull(result.getVersion());
        assertNotNull(result.getHostname());
    }

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

    @Test(expected = ForbiddenException.class)
    public void testHtmlPageNoSession() {
        WebTarget webTarget = ClientBuilder
                .newClient()
                .target(getApiBasePath())
                .path("/html");

        webTarget
                .request()
                .get(String.class);
    }
}
