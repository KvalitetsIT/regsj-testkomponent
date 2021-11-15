package dk.kvalitetsit.hello.session;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;
import java.util.Map;

public class SessionData {

    private Map<String,List<String>> userAttributes;

    public Map<String, List<String>> getUserAttributes() {
        return userAttributes;
    }

    @JsonSetter("UserAttributes")
    public void setUserAttributes(Map<String, List<String>> userAttributes) {
        this.userAttributes = userAttributes;
    }

    public List<String> getUserAttributes(String userAttribute) {
        if (userAttributes != null && userAttributes.containsKey(userAttribute)) {
            List<String> ual = userAttributes.get(userAttribute);
            if (ual != null && ual.size() > 0) {
                return ual;
            }
        }
        return null;
    }

    public boolean containsUserAttributes() {
        return userAttributes != null;
    }

    public String getUserAttribute(String userAttribute) {
        List<String> userAttributes = getUserAttributes(userAttribute);
        if (userAttributes != null && userAttributes.size() > 0) {
            return userAttributes.get(0);
        }
        return null;
    }
}
