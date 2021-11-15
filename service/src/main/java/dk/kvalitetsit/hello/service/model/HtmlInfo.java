package dk.kvalitetsit.hello.service.model;

public class HtmlInfo {
    private String version;
    private String hostName;
    private String configurableText;
    private String environment;

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

    public void setConfigurableText(String configurableText) {
        this.configurableText = configurableText;
    }

    public String getConfigurableText() {
        return configurableText;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }
}
