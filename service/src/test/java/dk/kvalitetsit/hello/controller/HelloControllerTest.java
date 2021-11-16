package dk.kvalitetsit.hello.controller;

import dk.kvalitetsit.hello.service.HelloService;
import dk.kvalitetsit.hello.service.model.HelloServiceOutput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class HelloControllerTest {
    private HelloController helloController;
    private HelloService helloService;

    @Before
    public void setup() {
        helloService = Mockito.mock(HelloService.class);

        helloController = new HelloController(helloService);
    }

    @Test
    public void testCallController() {
        var expectedContext = new HashMap<String, List<String>>();
        expectedContext.put("a1", Arrays.asList("v1", "v2"));
        expectedContext.put("a2", Collections.singletonList("v1"));

        Mockito.when(helloService.helloServiceBusinessLogic()).then(a -> {
            HelloServiceOutput output = new HelloServiceOutput();
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
