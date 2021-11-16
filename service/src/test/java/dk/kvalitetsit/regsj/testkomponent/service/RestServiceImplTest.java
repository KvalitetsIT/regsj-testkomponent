package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class RestServiceImplTest {
    private RestService restService;
    private UserContextService userContextService;

    @Before
    public void setup() {
        userContextService = Mockito.mock(UserContextService.class);
        restService = new RestServiceImpl(userContextService);
    }

    @Test
    public void testValidInput() {
        var expectedUserAttributes = new HashMap<String, List<String>>();
        Mockito.when(userContextService.getUserAttributes()).thenReturn(expectedUserAttributes);

        var result = restService.helloServiceBusinessLogic();
        assertNotNull(result);
        Assert.assertEquals(expectedUserAttributes, result.getUserContext());
    }
}
