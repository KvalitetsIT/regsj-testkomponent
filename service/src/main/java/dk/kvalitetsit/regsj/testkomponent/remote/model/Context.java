package dk.kvalitetsit.regsj.testkomponent.remote.model;

import java.util.List;

public class Context {
    private String attributeName;

    private List<String> attributeValue;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public List<String> getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(List<String> attributeValue) {
        this.attributeValue = attributeValue;
    }
}
