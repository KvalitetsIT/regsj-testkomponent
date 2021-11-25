package dk.kvalitetsit.regsj.testkomponent.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.kvalitetsit.logging.RequestIdGenerator;
import dk.kvalitetsit.regsj.testkomponent.remote.model.ContextResponse;
import dk.kvalitetsit.regsj.testkomponent.remote.model.HelloResponse;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

public class TestkomponentClientImpl implements TestkomponentClient {
    private Logger logger = LoggerFactory.getLogger(TestkomponentClientImpl.class);

    private final RestTemplate template;
    private final String endpoint;
    private final UserContextService userContextService;
    private final String endpointProtected;
    private final RequestIdGenerator requestIdGenerator;
    private static final String CLAIM_HEADER = "X-Claims";
    private static final String REQUEST_ID_HEADER = "x-request-id";

    public TestkomponentClientImpl(String endpoint, RequestIdGenerator requestIdGenerator, UserContextService userContextService, String endpointProtected) {
        this.requestIdGenerator = requestIdGenerator;
        this.endpoint = endpoint;
        this.userContextService = userContextService;
        this.endpointProtected = endpointProtected;

        template = new RestTemplate();
    }

    @Override
    public HelloResponse callTestClient() {
        var headers = new HttpHeaders();
        headers.put(REQUEST_ID_HEADER, Collections.singletonList(requestIdGenerator.getOrGenerateRequestId()));
        headers.put(CLAIM_HEADER, Collections.singletonList(createClaimHeader()));

        final HttpEntity<String> entity = new HttpEntity<>(headers);

        var response = template
                .exchange(endpoint, HttpMethod.GET, entity, HelloResponse.class);

        return response.getBody();
    }


    @Override
    public ContextResponse callTestClientProtected() {
        var headers = new HttpHeaders();
        headers.put(REQUEST_ID_HEADER, Collections.singletonList(requestIdGenerator.getOrGenerateRequestId()));
        headers.put(CLAIM_HEADER, Collections.singletonList(createClaimHeader()));

        final HttpEntity<String> entity = new HttpEntity<>(headers);

        var response = template
                .exchange(endpointProtected, HttpMethod.GET, entity, ContextResponse.class);

        return response.getBody();
    }

    private String createClaimHeader() {
        var allowedClaims = Arrays.asList("dk:regsj:organisation_id", "roles", "username", "urn:oid:2.5.4.4", "urn:oid:2.5.4.42", "urn:oid:1.2.840.113549.1.9.1");

        var attributes = userContextService.getUserAttributes();
        var claims = new ArrayList<Claim>();

        attributes.forEach((k,v) -> {
            if(allowedClaims.contains(k)) {
                var c = new Claim();
                c.setKey(k);
                c.setValue(String.join(",", v));
                claims.add(c);
            }
        });

        try {
            return Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsBytes(claims));
        }
        catch(JsonProcessingException e) {
            logger.error("Error serializing data.", e);
            throw new RuntimeException(e);

        }
    }
}

class Claim {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
