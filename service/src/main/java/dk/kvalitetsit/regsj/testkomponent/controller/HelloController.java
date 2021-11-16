package dk.kvalitetsit.regsj.testkomponent.controller;

import dk.kvalitetsit.regsj.testkomponent.service.HelloService;
import org.openapitools.api.KithugsApi;
import org.openapitools.model.Context;
import org.openapitools.model.ContextResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements KithugsApi {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public ResponseEntity<ContextResponse> restV1ContextGet() {
        var contextInformation = helloService.helloServiceBusinessLogic();

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
