package dk.kvalitetsit.hello.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class UserContextServiceImpl implements UserContextService {
    private final Logger logger = LoggerFactory.getLogger(UserContextServiceImpl.class);
    private final HttpServletRequest request;
    private final String userContextHeaderName;
    private SessionData sessionData;
    private final String userAttributesOrgKey;

    public UserContextServiceImpl(HttpServletRequest request, String userContextHeaderName, String userAttributesOrgKey) {
        this.request = request;
        this.userContextHeaderName = userContextHeaderName;
        this.userAttributesOrgKey = userAttributesOrgKey;
    }

    public String getOrganisation() {
        SessionData sessionData = readSessionData();
        if (sessionData.getUserAttributes().containsKey(userAttributesOrgKey)) {
            return sessionData.getUserAttribute(userAttributesOrgKey);
        }

        return null;
    }

    private SessionData readSessionData() {
        if(sessionData == null) {
            String header = request.getHeader(userContextHeaderName);
            logger.debug("Session header found. Value: {}", header);
            var sessionParser = new SessionParser();

            sessionData = sessionParser.getSessionData(header).orElseThrow(() -> new RuntimeException("Invalid request"));
        }

        return sessionData;
    }
}
