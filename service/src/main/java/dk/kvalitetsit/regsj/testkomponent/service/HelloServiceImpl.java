package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutput;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;

public class HelloServiceImpl implements HelloService {
    private final UserContextService userContextService;

    public HelloServiceImpl(UserContextService userContextService) {
        this.userContextService = userContextService;
    }

    @Override
    public HelloServiceOutput helloServiceBusinessLogic() {
        var helloServiceOutput = new HelloServiceOutput();
        helloServiceOutput.setUserContext(userContextService.getUserAttributes());

        return helloServiceOutput;
    }
}
