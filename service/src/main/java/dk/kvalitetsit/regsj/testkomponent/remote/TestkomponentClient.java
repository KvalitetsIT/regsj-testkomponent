package dk.kvalitetsit.regsj.testkomponent.remote;

import dk.kvalitetsit.regsj.testkomponent.remote.model.ContextResponse;
import dk.kvalitetsit.regsj.testkomponent.remote.model.HelloResponse;

public interface TestkomponentClient {
    HelloResponse callTestClient();

    ContextResponse callTestClientProtected();
}
