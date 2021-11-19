package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutput;
import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutputProtected;

import java.net.UnknownHostException;

public interface RestService {
    HelloServiceOutput helloServiceBusinessLogic() throws UnknownHostException;

    HelloServiceOutputProtected helloServiceBusinessLogicProtected();
}
