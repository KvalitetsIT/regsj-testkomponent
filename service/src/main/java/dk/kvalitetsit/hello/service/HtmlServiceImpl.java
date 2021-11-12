package dk.kvalitetsit.hello.service;

import dk.kvalitetsit.hello.service.model.HtmlInfo;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class HtmlServiceImpl implements HtmlService {
    private final VersionProvider VersionProvider;

    public HtmlServiceImpl(VersionProvider VersionProvider) {
        this.VersionProvider = VersionProvider;
    }

    @Override
    public HtmlInfo getHtmlInfo() throws UnknownHostException {
        var htmlInfo = new HtmlInfo();
        htmlInfo.setVersion(VersionProvider.getVersion());
        htmlInfo.setHostName(Inet4Address.getLocalHost().getHostName());

        return htmlInfo;
    }
}
