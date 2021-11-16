package dk.kvalitetsit.hello.service;

import dk.kvalitetsit.hello.session.UserContextService;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HtmlServiceImplTest {
    private VersionProvider versionProvider;
    private String configurableText;
    private HtmlServiceImpl htmlService;
    private String environment;
    private UserContextService userContextService;

    @Before
    public void setup() {
        versionProvider = Mockito.mock(VersionProvider.class);
        configurableText = "some text";
        environment = "dev";
        userContextService = Mockito.mock(UserContextService.class);
        htmlService = new HtmlServiceImpl(versionProvider, configurableText, environment, userContextService);
    }

    @Test
    public void testGetInformation() throws UnknownHostException {
        var version = "some_version";
        Mockito.when(versionProvider.getVersion()).thenReturn(version);

        Mockito.when(userContextService.getUserAttributes()).then(x -> {
           var context = new HashMap<String, List<String>>();
           context.put("roles", Arrays.asList("rolle1", "rolle2"));
           context.put("organisation", Collections.singletonList("some_org"));

           return context;
        });

        var result = htmlService.getHtmlInfo();

        assertNotNull(result);
        assertEquals(configurableText, result.getConfigurableText());
        assertEquals(environment, result.getEnvironment());
        assertEquals(version, result.getVersion());
        assertNotNull(result.getHostName());
        assertEquals(2, result.getUserContextInformation().size());
        assertEquals("[some_org]", result.getUserContextInformation().get("organisation").toString());
        assertEquals("[rolle1, rolle2]", result.getUserContextInformation().get("roles").toString());
    }
}