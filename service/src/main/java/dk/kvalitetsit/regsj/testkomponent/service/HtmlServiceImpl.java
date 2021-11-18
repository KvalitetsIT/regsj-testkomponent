package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.remote.TestkomponentClient;
import dk.kvalitetsit.regsj.testkomponent.service.model.HtmlInfo;
import dk.kvalitetsit.regsj.testkomponent.service.model.ServiceCallResponse;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;

public class HtmlServiceImpl implements HtmlService {
    private static final Logger logger = LoggerFactory.getLogger(HtmlServiceImpl.class);

    private final VersionProvider VersionProvider;
    private final String configurableText;
    private final String environment;
    private final UserContextService userContextService;
    private final boolean doServiceCall;
    private final TestkomponentClient testkomponentClient;

    public HtmlServiceImpl(VersionProvider VersionProvider, String configurableText, String environment, UserContextService userContextService, boolean doServiceCall, TestkomponentClient testkomponentClient) {
        this.VersionProvider = VersionProvider;
        this.configurableText = configurableText;
        this.environment = environment;
        this.userContextService = userContextService;
        this.doServiceCall = doServiceCall;
        this.testkomponentClient = testkomponentClient;
    }

    @Override
    public HtmlInfo getHtmlInfo() throws UnknownHostException {
        logger.info("Nu laver vi forretningslogik.");

        var htmlInfo = new HtmlInfo();
        htmlInfo.setVersion(VersionProvider.getVersion());
        htmlInfo.setHostName(Inet4Address.getLocalHost().getHostName());
        htmlInfo.setConfigurableText(configurableText);
        htmlInfo.setEnvironment(environment);
        htmlInfo.setUserContextInformation(userContextService.getUserAttributes());

        if(doServiceCall) {
            var testkomponentResponse = testkomponentClient.callTestClient();

            var serviceCallResponse = new ServiceCallResponse();
            serviceCallResponse.setContext(new HashMap<>());
            htmlInfo.setServiceCallResponse(serviceCallResponse);

            testkomponentResponse.getContext().forEach( context -> serviceCallResponse.getContext().put(context.getAttributeName(), String.join(", ", context.getAttributeValue())));
        }


        return htmlInfo;
    }
}
