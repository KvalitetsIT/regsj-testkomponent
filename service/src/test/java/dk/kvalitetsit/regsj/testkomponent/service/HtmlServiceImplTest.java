package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.dao.LastAccessedDao;
import dk.kvalitetsit.regsj.testkomponent.dao.entity.LastAccessed;
import dk.kvalitetsit.regsj.testkomponent.remote.TestkomponentClient;
import dk.kvalitetsit.regsj.testkomponent.remote.model.HelloResponse;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

public class HtmlServiceImplTest {
    private VersionProvider versionProvider;
    private String configurableText;
    private HtmlServiceImpl htmlService;
    private String environment;
    private UserContextService userContextService;
    private TestkomponentClient testkomponentClient;
    private LastAccessedDao lastAccessedDao;

    @Before
    public void setup() {
        versionProvider = Mockito.mock(VersionProvider.class);
        configurableText = "some text";
        environment = "dev";
        userContextService = Mockito.mock(UserContextService.class);
        testkomponentClient = Mockito.mock(TestkomponentClient.class);
        lastAccessedDao = Mockito.mock(LastAccessedDao.class);

        htmlService = new HtmlServiceImpl(versionProvider, configurableText, environment, userContextService, true, testkomponentClient, lastAccessedDao);
    }

    @Test
    public void testGetInformation() throws UnknownHostException {
        var version = "some_version";
        Mockito.when(versionProvider.getVersion()).thenReturn(version);

        var now = LocalDateTime.now();

        Mockito.when(lastAccessedDao.getLatest()).then(x -> {
            LastAccessed lastAccessed = new LastAccessed();
            lastAccessed.setAccessTime(now);

            return Optional.of(lastAccessed);
        });

        Mockito.when(testkomponentClient.callTestClient()).then(x -> {
            var helloResponse = new HelloResponse();
            helloResponse.setVersion("1.0.0");
            helloResponse.setHostname("localhost");

            return helloResponse;
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
        assertEquals(now, result.getLastAccess().get());

        assertTrue(result.getServiceCallResponse().isPresent());
        var serviceCalLResponse = result.getServiceCallResponse().get();
        assertEquals("1.0.0", serviceCalLResponse.getVersion());
        assertEquals("localhost", serviceCalLResponse.getHostname());

        Mockito.verify(lastAccessedDao, times(1)).getLatest();
        Mockito.verify(lastAccessedDao, times(1)).insert(Mockito.any(LastAccessed.class));
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

        var service = new HtmlServiceImpl(versionProvider, configurableText, environment, userContextService, false, testkomponentClient, lastAccessedDao);
        var result = service.getHtmlInfo();

        assertNotNull(result);
        assertTrue(result.getServiceCallResponse().isEmpty());
        assertTrue(result.getLastAccess().isEmpty());
    }
}
