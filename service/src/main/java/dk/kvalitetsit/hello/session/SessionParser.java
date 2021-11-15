package dk.kvalitetsit.hello.session;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class SessionParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionParser.class);
    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public Optional<SessionData> getSessionData(String encodedSessionData) {
        try {
            LOGGER.debug("Encoded session information: {}", encodedSessionData);
            String decoded = new String(Base64.getDecoder().decode(encodedSessionData));
            LOGGER.debug("Decoded session information: " + decoded);
            SessionData sessionData = mapper.readValue(decoded, SessionData.class);
            if (!sessionData.containsUserAttributes()) {
                LOGGER.error("Failed to find any user attributes in header: {}", decoded);
                return Optional.empty();
            }

            return Optional.of(sessionData);
        }
        catch(NullPointerException e) {
            LOGGER.error("Called with null value", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to decode header value: " + encodedSessionData, e);
        } catch (IOException e) {
            LOGGER.error("Failed to parse header value: " + encodedSessionData, e);
        }

        return Optional.empty();
    }
}
