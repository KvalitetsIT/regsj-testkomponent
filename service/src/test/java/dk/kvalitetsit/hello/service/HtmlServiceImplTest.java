package dk.kvalitetsit.hello.service;

import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HtmlServiceImplTest {
    private VersionProvider versionProvider;
    private String configurableText;
    private HtmlServiceImpl htmlService;
    private String environment;

    @Before
    public void setup() {
        versionProvider = Mockito.mock(VersionProvider.class);
        configurableText = "some text";
        environment = "dev";
        htmlService = new HtmlServiceImpl(versionProvider, configurableText, environment);
    }

    @Test
    public void testGetInformation() throws UnknownHostException {
        var version = "some_version";
        Mockito.when(versionProvider.getVersion()).thenReturn(version);

        var result = htmlService.getHtmlInfo();

        assertNotNull(result);
        assertEquals(configurableText, result.getConfigurableText());
        assertEquals(environment, result.getEnvironment());
        assertEquals(version, result.getVersion());
        assertNotNull(result.getHostName());

    }

}
