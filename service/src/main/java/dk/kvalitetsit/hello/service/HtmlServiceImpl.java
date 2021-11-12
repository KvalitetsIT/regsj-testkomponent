package dk.kvalitetsit.hello.service;

import dk.kvalitetsit.hello.service.model.HtmlInfo;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class HtmlServiceImpl implements HtmlService {
    private final VersionProvider VersionProvider;
    private final String configurableText;

    public HtmlServiceImpl(VersionProvider VersionProvider, String configurableText) {
        this.VersionProvider = VersionProvider;
        this.configurableText = configurableText;
    }

    @Override
    public HtmlInfo getHtmlInfo() throws UnknownHostException {
        var htmlInfo = new HtmlInfo();
        htmlInfo.setVersion(VersionProvider.getVersion());
        htmlInfo.setHostName(Inet4Address.getLocalHost().getHostName());
        htmlInfo.setConfigurableText(configurableText);

        return htmlInfo;
    }
}
