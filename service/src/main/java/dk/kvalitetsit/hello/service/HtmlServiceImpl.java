package dk.kvalitetsit.hello.service;

import dk.kvalitetsit.hello.service.model.HtmlInfo;
import dk.kvalitetsit.hello.session.UserContextService;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class HtmlServiceImpl implements HtmlService {
    private static final Logger logger = LoggerFactory.getLogger(HtmlServiceImpl.class);

    private final VersionProvider VersionProvider;
    private final String configurableText;
    private final String environment;
    private final UserContextService userContextService;

    public HtmlServiceImpl(VersionProvider VersionProvider, String configurableText, String environment, UserContextService userContextService) {
        this.VersionProvider = VersionProvider;
        this.configurableText = configurableText;
        this.environment = environment;
        this.userContextService = userContextService;
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

        return htmlInfo;
    }
}
