package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import dk.medcom.audit.client.AuditClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RestServiceImplTest {
    private RestService restService;
    private VersionProvider versionProvider;
    private UserContextService userContextService;
    private AuditClient auditClient;

    @Before
    public void setup() {
        versionProvider = Mockito.mock(VersionProvider.class);
        userContextService = Mockito.mock(UserContextService.class);
        auditClient = Mockito.mock(AuditClient.class);

        restService = new RestServiceImpl(versionProvider, userContextService, auditClient);
    }

    @Test
    public void testValidInput() throws UnknownHostException {
        var expectedUserAttributes = new HashMap<String, List<String>>();
        Mockito.when(versionProvider.getVersion()).thenReturn("1.0.0");

        var result = restService.helloServiceBusinessLogic();
        assertNotNull(result);
        assertEquals("1.0.0", result.getVersion());
        assertNotNull(result.getHostName());
    }

    @Test
    public void testValidInputProtected() {
        var expectedUserAttributes = new HashMap<String, List<String>>();
        Mockito.when(userContextService.getUserAttributes()).thenReturn(expectedUserAttributes);

        var result = restService.helloServiceBusinessLogicProtected();
        assertNotNull(result);
        Assert.assertEquals(expectedUserAttributes, result.getUserContext());
    }
}
