package dk.kvalitetsit.hello.service;

import dk.kvalitetsit.hello.service.model.HelloServiceOutput;
import dk.kvalitetsit.hello.session.UserContextService;

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
