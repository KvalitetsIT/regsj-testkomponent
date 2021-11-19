package dk.kvalitetsit.regsj.testkomponent.controller;

import dk.kvalitetsit.regsj.testkomponent.service.RestService;
import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutput;
import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutputProtected;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RestControllerTest {
    private RestController helloController;
    private RestService restService;

    @Before
    public void setup() {
        restService = Mockito.mock(RestService.class);

        helloController = new RestController(restService);
    }

    @Test
    public void testCallController() throws UnknownHostException {
        var expectedContext = new HashMap<String, List<String>>();
        expectedContext.put("a1", Arrays.asList("v1", "v2"));
        expectedContext.put("a2", Collections.singletonList("v1"));

        Mockito.when(restService.helloServiceBusinessLogic()).then(a -> {
            HelloServiceOutput output = new HelloServiceOutput();
            output.setVersion("1.0.0");
            output.setHostName("localhost");
            return output;
        });

        var result = helloController.restV1HelloGet();

        assertNotNull(result);
        assertEquals("localhost", result.getBody().getHostname());
        assertEquals("1.0.0", result.getBody().getVersion());
    }

    @Test
    public void testCallControllerContext() {
        var expectedContext = new HashMap<String, List<String>>();
        expectedContext.put("a1", Arrays.asList("v1", "v2"));
        expectedContext.put("a2", Collections.singletonList("v1"));

        Mockito.when(restService.helloServiceBusinessLogicProtected()).then(a -> {
            var output = new HelloServiceOutputProtected();
            output.setUserContext(expectedContext);
            return output;
        });

        var result = helloController.restV1ContextGet();

        assertNotNull(result);
        assertEquals(2, result.getBody().getContext().size());
        assertEquals("a1", result.getBody().getContext().get(0).getAttributeName());
        assertEquals("[v1, v2]", result.getBody().getContext().get(0).getAttributeValue().toString());

        assertEquals("a2", result.getBody().getContext().get(1).getAttributeName());
        assertEquals("[v1]", result.getBody().getContext().get(1).getAttributeValue().toString());
    }
}
