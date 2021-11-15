package dk.kvalitetsit.hello.controller;

import dk.kvalitetsit.hello.service.HtmlService;
import dk.kvalitetsit.hello.service.model.HtmlInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.UnknownHostException;

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

        Mockito.when(htmlService.getHtmlInfo()).then(a -> {
            var output = new HtmlInfo();
            output.setHostName(expectedHostName);
            output.setConfigurableText(expectedText);
            output.setEnvironment(expectedEnvironment);
            output.setVersion(expectedVersion);

            return output;
        });

        var result = indexController.get();

        assertNotNull(result);
        assertEquals("index", result.getViewName());
        assertEquals(expectedVersion, result.getModel().get("version"));
        assertEquals(expectedEnvironment, result.getModel().get("environment"));
        assertEquals(expectedHostName, result.getModel().get("hostname"));
        assertEquals(expectedText, result.getModel().get("configurableText"));
    }
}
