package dk.kvalitetsit.regsj.testkomponent.service.model;

import java.util.List;
import java.util.Map;

public class HelloServiceOutput {
    private String hostName;
    private String version;

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
