package dk.kvalitetsit.regsj.testkomponent.service.model;

import java.util.HashMap;

public class ServiceCallResponse {
    private String hostname;
    private String version;
    private HashMap<String, String> context;

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

    public void setContext(HashMap<String, String> context) {
        this.context = context;
    }

    public HashMap<String, String> getContext() {
        return context;
    }
}
