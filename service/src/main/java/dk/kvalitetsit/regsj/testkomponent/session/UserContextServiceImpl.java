package dk.kvalitetsit.regsj.testkomponent.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class UserContextServiceImpl implements UserContextService {
    private final Logger logger = LoggerFactory.getLogger(UserContextServiceImpl.class);
    private final HttpServletRequest request;
    private final String userContextHeaderName;
    private SessionData sessionData;

    public UserContextServiceImpl(HttpServletRequest request, String userContextHeaderName) {
        this.request = request;
        this.userContextHeaderName = userContextHeaderName;
    }

    @Override
    public Map<String, List<String>> getUserAttributes() {
        var sessionData = readSessionData();
        return sessionData.getUserAttributes();
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
