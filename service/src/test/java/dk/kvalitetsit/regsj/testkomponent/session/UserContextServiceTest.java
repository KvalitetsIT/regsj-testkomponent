package dk.kvalitetsit.regsj.testkomponent.session;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;

public class UserContextServiceTest {
    private final String headerName = "X-HEADER";
    private final String orgKey = "Organisation";
    private UserContextServiceImpl userContextService;
    private HttpServletRequest request;

    @Before
    public void setup() {
        request = Mockito.mock(HttpServletRequest.class);
        userContextService = new UserContextServiceImpl(request, headerName);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidHeaderThrowsException() {
        Mockito.when(request.getHeader(headerName)).thenReturn("ÆÅØ");

        userContextService.getUserAttributes();
    }

    @Test(expected = RuntimeException.class)
    public void testMissingHeader() {
        Mockito.when(request.getHeader(headerName)).thenReturn(null);

        userContextService.getUserAttributes();
    }

    @Test
    public void testIsPresentTrue() {
        var input = "ew0KICAgICJVc2VyQXR0cmlidXRlcyI6IHsNCiAgICAgICAgIlVzZXJSb2xlcyI6IFsNCiAgICAgICAgICAgICJwcm92aXNpb25lcnJvbGUiDQogICAgICAgIF0sDQogICAgICAgICJPcmdhbmlzYXRpb24iOiBbInNvbWVfb3JnIl0NCiAgICB9DQp9";
        Mockito.when(request.getHeader(headerName)).thenReturn(input);

        assertTrue(userContextService.isPresentAndValid());
    }

    @Test
    public void testIsPresentFalse() {
        var input = "e30K";
        Mockito.when(request.getHeader(headerName)).thenReturn(input);

        assertFalse(userContextService.isPresentAndValid());
    }

    @Test
    public void testGetUserAttributes() {
        var input = "ew0KICAgICJVc2VyQXR0cmlidXRlcyI6IHsNCiAgICAgICAgIlVzZXJSb2xlcyI6IFsNCiAgICAgICAgICAgICJwcm92aXNpb25lcnJvbGUiDQogICAgICAgIF0sDQogICAgICAgICJPcmdhbmlzYXRpb24iOiBbInNvbWVfb3JnIl0NCiAgICB9DQp9";
        Mockito.when(request.getHeader(headerName)).thenReturn(input);

        var userAttributes = userContextService.getUserAttributes();
        assertNotNull(userAttributes);
        assertEquals(2, userAttributes.size());
        assertEquals("[provisionerrole]", userAttributes.get("UserRoles").toString());
        assertEquals("[some_org]", userAttributes.get("Organisation").toString());
    }
}
