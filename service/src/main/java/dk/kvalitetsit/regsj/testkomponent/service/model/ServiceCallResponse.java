package dk.kvalitetsit.regsj.testkomponent.service.model;

public class ServiceCallResponse {
    private String hostname;
    private String version;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
