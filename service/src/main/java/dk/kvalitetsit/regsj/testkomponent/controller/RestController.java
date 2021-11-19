package dk.kvalitetsit.regsj.testkomponent.controller;

import dk.kvalitetsit.regsj.testkomponent.service.RestService;
import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutput;
import dk.kvalitetsit.regsj.testkomponent.session.UserContext;
import org.openapitools.api.TestKomponentApi;
import org.openapitools.model.Context;
import org.openapitools.model.ContextResponse;
import org.openapitools.model.HelloResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.net.UnknownHostException;

@org.springframework.web.bind.annotation.RestController
public class RestController implements TestKomponentApi {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);
    private final RestService restService;

    public RestController(RestService restService) {
        this.restService = restService;
    }

    @Override
    public ResponseEntity<HelloResponse> restV1HelloGet() {
        HelloServiceOutput helloServiceOutput;
        try {
            helloServiceOutput = restService.helloServiceBusinessLogic();
        } catch (UnknownHostException e) {
            logger.error("Error during business logic.");
            throw new RuntimeException(e);
        }

        var response = new HelloResponse();
        response.setVersion(helloServiceOutput.getVersion());
        response.setHostname(helloServiceOutput.getHostName());

        return ResponseEntity.ok(response);
    }

    @Override
    @UserContext
    public ResponseEntity<ContextResponse> restV1ContextGet() {
        var contextInformation = restService.helloServiceBusinessLogicProtected();

        var response = new ContextResponse();

        contextInformation.getUserContext().forEach((k, v) -> {
            var contextItem = new Context();
            contextItem.setAttributeName(k);
            contextItem.setAttributeValue(v);

            response.addContextItem(contextItem);
        });

        return ResponseEntity.ok(response);
    }
}
