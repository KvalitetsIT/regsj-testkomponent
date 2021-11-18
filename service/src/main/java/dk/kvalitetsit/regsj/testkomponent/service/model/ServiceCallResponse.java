package dk.kvalitetsit.regsj.testkomponent.service.model;

import java.util.Map;

public class ServiceCallResponse {
    private Map<String, String> context;

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }
}
