package dk.kvalitetsit.regsj.testkomponent.controller;

import dk.kvalitetsit.regsj.testkomponent.service.HtmlService;
import dk.kvalitetsit.regsj.testkomponent.service.model.HtmlInfo;
import dk.kvalitetsit.regsj.testkomponent.service.model.ServiceCallResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class IndexControllerTest {
    private IndexController indexController;
    private HtmlService htmlService;

    @Before
    public void setup() {
        htmlService = Mockito.mock(HtmlService.class);

        indexController = new IndexController(htmlService);
    }

    @Test
    public void testCallController() throws UnknownHostException {
        var expectedVersion = "some_version";
        var expectedHostName = "some_hostname";
        var expectedText = "some_text";
        var expectedEnvironment = "dev";
        var expectedUserContext = new HashMap<String, List<String>>();
        var expectedRemoteVersion = "1.0.0";
        var expectedRemoteHostname = "localhost";
        var expectedRemoteResponse = new HashMap<String, String>();

        Mockito.when(htmlService.getHtmlInfo()).then(a -> {
            var output = new HtmlInfo();
            output.setHostName(expectedHostName);
            output.setConfigurableText(expectedText);
            output.setEnvironment(expectedEnvironment);
            output.setVersion(expectedVersion);
            output.setUserContextInformation(expectedUserContext);

            var serviceCallResponse = new ServiceCallResponse();
            serviceCallResponse.setHostname(expectedRemoteHostname);
            serviceCallResponse.setVersion(expectedRemoteVersion);
            serviceCallResponse.setContext(expectedRemoteResponse);
            output.setServiceCallResponse(serviceCallResponse);

            return output;
        });

        var result = indexController.get();

        assertNotNull(result);
        assertEquals("index", result.getViewName());
        assertEquals(expectedVersion, result.getModel().get("version"));
        assertEquals(expectedEnvironment, result.getModel().get("environment"));
        assertEquals(expectedHostName, result.getModel().get("hostname"));
        assertEquals(expectedText, result.getModel().get("configurableText"));
        assertEquals(expectedUserContext, result.getModel().get("userContext"));
        assertEquals(expectedRemoteHostname, result.getModel().get("remoteHostname"));
        assertEquals(expectedRemoteVersion, result.getModel().get("remoteVersion"));
        assertEquals(expectedRemoteResponse, result.getModel().get("remoteResponse"));
    }
}
