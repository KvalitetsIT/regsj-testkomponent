package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutput;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;

public class RestServiceImpl implements RestService {
    private final UserContextService userContextService;

    public RestServiceImpl(UserContextService userContextService) {
        this.userContextService = userContextService;
    }

    @Override
    public HelloServiceOutput helloServiceBusinessLogic() {
        var helloServiceOutput = new HelloServiceOutput();
        helloServiceOutput.setUserContext(userContextService.getUserAttributes());

        return helloServiceOutput;
    }
}
