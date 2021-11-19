package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutput;
import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutputProtected;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class RestServiceImpl implements RestService {
    private final VersionProvider versionProvider;
    private final UserContextService userContextService;

    public RestServiceImpl(VersionProvider versionProvider, UserContextService userContextService) {
        this.versionProvider = versionProvider;
        this.userContextService = userContextService;
    }

    @Override
    public HelloServiceOutput helloServiceBusinessLogic() throws UnknownHostException {
        var helloServiceOutput = new HelloServiceOutput();
        helloServiceOutput.setVersion(versionProvider.getVersion());
        helloServiceOutput.setHostName(Inet4Address.getLocalHost().getHostName());

        return helloServiceOutput;
    }

    @Override
    public HelloServiceOutputProtected helloServiceBusinessLogicProtected() {
        var helloServiceOutput = new HelloServiceOutputProtected();
        helloServiceOutput.setUserContext(userContextService.getUserAttributes());

        return helloServiceOutput;
    }
}
