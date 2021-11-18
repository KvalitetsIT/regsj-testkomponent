package dk.kvalitetsit.regsj.testkomponent.remote;

import dk.kvalitetsit.logging.RequestIdGenerator;
import dk.kvalitetsit.regsj.testkomponent.remote.model.ContextResponse;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

public class TestkomponentClientImpl implements TestkomponentClient {
    private final RestTemplate template;
    private final String endpoint;
    private final UserContextService userContextService;
    private final RequestIdGenerator requestIdGenerator;
    private static final String CLAIM_HEADER = "X-Claims";
    private static final String REQUEST_ID_HEADER = "x-request-id";

    public TestkomponentClientImpl(String endpoint, RequestIdGenerator requestIdGenerator, UserContextService userContextService) {
        this.requestIdGenerator = requestIdGenerator;
        this.endpoint = endpoint;
        this.userContextService = userContextService;

        template = new RestTemplate();
    }

    @Override
    public ContextResponse callTestClient() {
        var headers = new HttpHeaders();
        headers.put(REQUEST_ID_HEADER, Collections.singletonList(requestIdGenerator.getOrGenerateRequestId()));
        headers.put(CLAIM_HEADER, Collections.singletonList(createClaimHeader(userContextService.getOrganisation())));

        final HttpEntity<String> entity = new HttpEntity<>(headers);

        var response = template
                .exchange(endpoint, HttpMethod.GET, entity, ContextResponse.class);

        return response.getBody();
    }

    private String createClaimHeader(String organization) {
        return Base64.getEncoder().encodeToString(String.format("[{\"key\": \"dk:regsj:organisation_id\",\"value\": \"%s\"}]", organization).getBytes());
    }
}
