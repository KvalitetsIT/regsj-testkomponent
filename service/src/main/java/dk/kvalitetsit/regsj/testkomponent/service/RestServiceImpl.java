package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutput;
import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutputProtected;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import dk.medcom.audit.client.AuditClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class RestServiceImpl implements RestService {
    private final Logger logger = LoggerFactory.getLogger(RestServiceImpl.class);

    private final VersionProvider versionProvider;
    private final UserContextService userContextService;

    public RestServiceImpl(VersionProvider versionProvider, UserContextService userContextService, AuditClient auditClient) {
        this.versionProvider = versionProvider;
        this.userContextService = userContextService;
    }

    @Override
    public HelloServiceOutput helloServiceBusinessLogic() throws UnknownHostException {
        logger.info("Udfører helloService forretningslogik.");
        var helloServiceOutput = new HelloServiceOutput();
        helloServiceOutput.setVersion(versionProvider.getVersion());
        helloServiceOutput.setHostName(Inet4Address.getLocalHost().getHostName());

        return helloServiceOutput;
    }

    @Override
    public HelloServiceOutputProtected helloServiceBusinessLogicProtected() {
        logger.info("Udfører helloService beskyttet forretningslogik.");
        var helloServiceOutput = new HelloServiceOutputProtected();
        helloServiceOutput.setUserContext(userContextService.getUserAttributes());

        return helloServiceOutput;
    }
}
