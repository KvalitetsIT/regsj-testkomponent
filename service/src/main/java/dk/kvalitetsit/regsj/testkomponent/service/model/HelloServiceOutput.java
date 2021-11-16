package dk.kvalitetsit.regsj.testkomponent.service.model;

import java.util.List;
import java.util.Map;

public class HelloServiceOutput {
    private Map<String, List<String>> userContext;

    public Map<String, List<String>> getUserContext() {
        return userContext;
    }

    public void setUserContext(Map<String, List<String>> userContext) {
        this.userContext = userContext;
    }
}
