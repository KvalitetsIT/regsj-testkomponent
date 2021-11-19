package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutput;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class RestServiceImpl implements RestService {
    private final VersionProvider versionProvider;

    public RestServiceImpl(VersionProvider versionProvider) {
        this.versionProvider = versionProvider;
    }

    @Override
    public HelloServiceOutput helloServiceBusinessLogic() throws UnknownHostException {
        var helloServiceOutput = new HelloServiceOutput();
        helloServiceOutput.setVersion(versionProvider.getVersion());
        helloServiceOutput.setHostName(Inet4Address.getLocalHost().getHostName());

        return helloServiceOutput;
    }
}
