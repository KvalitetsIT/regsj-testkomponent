package dk.kvalitetsit.regsj.testkomponent.session;

import java.util.List;
import java.util.Map;

public interface UserContextService {
    Map<String, List<String>> getUserAttributes();

    boolean isPresentAndValid();

    String getOrganisation();
}
