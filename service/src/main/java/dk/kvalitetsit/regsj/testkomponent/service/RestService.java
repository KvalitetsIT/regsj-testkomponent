package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.service.model.HelloServiceOutput;

import java.net.UnknownHostException;

public interface RestService {
    HelloServiceOutput helloServiceBusinessLogic() throws UnknownHostException;
}
