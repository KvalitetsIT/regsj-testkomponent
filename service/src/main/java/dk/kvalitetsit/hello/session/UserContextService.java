package dk.kvalitetsit.hello.session;

import java.util.List;
import java.util.Map;

public interface UserContextService {
    Map<String, List<String>> getUserAttributes();
}
