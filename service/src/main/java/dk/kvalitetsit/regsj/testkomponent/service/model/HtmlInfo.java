package dk.kvalitetsit.regsj.testkomponent.service.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HtmlInfo {
    private String version;
    private String hostName;
    private String configurableText;
    private String environment;
    private Map<String, List<String>> userContextInformation;
    private ServiceCallResponse serviceCallResponse;

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

    public void setUserContextInformation(Map<String, List<String>> userContextInformation) {
        this.userContextInformation = userContextInformation;
    }

    public Map<String, List<String>> getUserContextInformation() {
        return userContextInformation;
    }

    public Optional<ServiceCallResponse> getServiceCallResponse() {
        return Optional.ofNullable(serviceCallResponse);
    }

    public void setServiceCallResponse(ServiceCallResponse serviceCallResponse) {
        this.serviceCallResponse = serviceCallResponse;
    }
}
