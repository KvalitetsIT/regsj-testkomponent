package dk.kvalitetsit.hello.service.model;

public class HtmlInfo {
    private String version;
    private String hostName;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }
}
