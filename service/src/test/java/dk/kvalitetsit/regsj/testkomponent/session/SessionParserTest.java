package dk.kvalitetsit.regsj.testkomponent.session;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SessionParserTest {
    @Test
    public void testParseSessionData() {
        var input = "eyAiVXNlckF0dHJpYnV0ZXMiOiB7ICJVc2VyUm9sZXMiOiBbInByb3Zpc2lvbmVycm9sZSJdIH19Cg==";
        var parser = new SessionParser();

        var result = parser.getSessionData(input);
        assertTrue(result.isPresent());
    }

    @Test
    public void testParseInvalidHeader() {
        var input = "xyzÆÅØ6";
        var parser = new SessionParser();

        var result = parser.getSessionData(input);
        assertFalse(result.isPresent());
    }

    @Test
    public void teetParseNullHeader() {
        var parser = new SessionParser();

        var result = parser.getSessionData(null);
        assertFalse(result.isPresent());
    }

    @Test
    public void testParseNoUserAttributes() {
        var input = "eyAieFVzZXJBdHRyaWJ1dGVzIjogeyAiVXNlclJvbGVzIjogWyJwcm92aXNpb25lcnJvbGUiXSB9fQo=";
        var parser = new SessionParser();

        var result = parser.getSessionData(input);
        assertFalse(result.isPresent());
    }
}
