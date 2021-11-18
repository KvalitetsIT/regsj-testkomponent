package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.remote.TestkomponentClient;
import dk.kvalitetsit.regsj.testkomponent.remote.model.Context;
import dk.kvalitetsit.regsj.testkomponent.remote.model.ContextResponse;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class HtmlServiceImplTest {
    private VersionProvider versionProvider;
    private String configurableText;
    private HtmlServiceImpl htmlService;
    private String environment;
    private UserContextService userContextService;
    private TestkomponentClient testkomponentClient;

    @Before
    public void setup() {
        versionProvider = Mockito.mock(VersionProvider.class);
        configurableText = "some text";
        environment = "dev";
        userContextService = Mockito.mock(UserContextService.class);
        testkomponentClient = Mockito.mock(TestkomponentClient.class);

        htmlService = new HtmlServiceImpl(versionProvider, configurableText, environment, userContextService, true, testkomponentClient);
    }

    @Test
    public void testGetInformation() throws UnknownHostException {
        var version = "some_version";
        Mockito.when(versionProvider.getVersion()).thenReturn(version);

        Mockito.when(testkomponentClient.callTestClient()).then(x -> {
            var firstContext = new Context();
            firstContext.setAttributeName("k1");
            firstContext.setAttributeValue(Arrays.asList("v1", "v2"));

            var secondContext = new Context();
            secondContext.setAttributeName("k2");
            secondContext.setAttributeValue(Collections.singletonList("v3"));

            var contextResponse = new ContextResponse();
            contextResponse.setContext(Arrays.asList(firstContext, secondContext));

            return contextResponse;
        });

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

        assertTrue(result.getServiceCallResponse().isPresent());
        var serviceCalLResponse = result.getServiceCallResponse().get();
        assertEquals(2, serviceCalLResponse.getContext().size());
        assertEquals("v1, v2", serviceCalLResponse.getContext().get("k1"));
        assertEquals("v3", serviceCalLResponse.getContext().get("k2"));
    }

    @Test
    public void testGetInformationNoServiceCall() throws UnknownHostException {
        var version = "some_version";
        Mockito.when(versionProvider.getVersion()).thenReturn(version);

        Mockito.when(userContextService.getUserAttributes()).then(x -> {
            var context = new HashMap<String, List<String>>();
            context.put("roles", Arrays.asList("rolle1", "rolle2"));
            context.put("organisation", Collections.singletonList("some_org"));

            return context;
        });

        var service = new HtmlServiceImpl(versionProvider, configurableText, environment, userContextService, false, testkomponentClient);
        var result = service.getHtmlInfo();

        assertNotNull(result);
        assertTrue(result.getServiceCallResponse().isEmpty());
    }
}
