package dk.kvalitetsit.hello.service;

import dk.kvalitetsit.hello.service.model.HtmlInfo;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class HtmlServiceImpl implements HtmlService {
    private static final Logger logger = LoggerFactory.getLogger(HtmlServiceImpl.class);

    private final VersionProvider VersionProvider;
    private final String configurableText;

    public HtmlServiceImpl(VersionProvider VersionProvider, String configurableText) {
        this.VersionProvider = VersionProvider;
        this.configurableText = configurableText;
    }

    @Override
    public HtmlInfo getHtmlInfo() throws UnknownHostException {
        logger.info("Nu laver vi forretningslogik.");

        var htmlInfo = new HtmlInfo();
        htmlInfo.setVersion(VersionProvider.getVersion());
        htmlInfo.setHostName(Inet4Address.getLocalHost().getHostName());
        htmlInfo.setConfigurableText(configurableText);

        return htmlInfo;
    }
}
