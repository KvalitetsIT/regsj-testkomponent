package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class HelloServiceImplTest {
    private HelloService helloService;
    private UserContextService userContextService;

    @Before
    public void setup() {
        userContextService = Mockito.mock(UserContextService.class);
        helloService = new HelloServiceImpl(userContextService);
    }

    @Test
    public void testValidInput() {
        var expectedUserAttributes = new HashMap<String, List<String>>();
        Mockito.when(userContextService.getUserAttributes()).thenReturn(expectedUserAttributes);

        var result = helloService.helloServiceBusinessLogic();
        assertNotNull(result);
        Assert.assertEquals(expectedUserAttributes, result.getUserContext());
    }
}
