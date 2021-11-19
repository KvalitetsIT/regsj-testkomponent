package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.dao.LastAccessedDao;
import dk.kvalitetsit.regsj.testkomponent.dao.entity.LastAccessed;
import dk.kvalitetsit.regsj.testkomponent.remote.TestkomponentClient;
import dk.kvalitetsit.regsj.testkomponent.service.model.HtmlInfo;
import dk.kvalitetsit.regsj.testkomponent.service.model.ServiceCallResponse;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class HtmlServiceImpl implements HtmlService {
    private static final Logger logger = LoggerFactory.getLogger(HtmlServiceImpl.class);

    private final VersionProvider VersionProvider;
    private final String configurableText;
    private final String environment;
    private final UserContextService userContextService;
    private final boolean doServiceCall;
    private final TestkomponentClient testkomponentClient;
    private final LastAccessedDao lastAccessedDao;

    public HtmlServiceImpl(VersionProvider VersionProvider,
                           String configurableText,
                           String environment,
                           UserContextService userContextService,
                           boolean doServiceCall,
                           TestkomponentClient testkomponentClient,
                           LastAccessedDao lastAccessedDao) {
        this.VersionProvider = VersionProvider;
        this.configurableText = configurableText;
        this.environment = environment;
        this.userContextService = userContextService;
        this.doServiceCall = doServiceCall;
        this.testkomponentClient = testkomponentClient;
        this.lastAccessedDao = lastAccessedDao;
    }

    @Override
    @Transactional
    public HtmlInfo getHtmlInfo() throws UnknownHostException {
        logger.info("Nu laver vi forretningslogik.");

        var lastAccessed = lastAccessedDao.getLatest();

        var htmlInfo = new HtmlInfo();
        htmlInfo.setVersion(VersionProvider.getVersion());
        htmlInfo.setHostName(Inet4Address.getLocalHost().getHostName());
        htmlInfo.setConfigurableText(configurableText);
        htmlInfo.setEnvironment(environment);
        htmlInfo.setUserContextInformation(userContextService.getUserAttributes());
        lastAccessed.ifPresent(x -> htmlInfo.setLastAccess(x.getAccessTime()));

        if(doServiceCall) {
            var testkomponentResponse = testkomponentClient.callTestClient();

            var serviceCallResponse = new ServiceCallResponse();
            serviceCallResponse.setHostname(testkomponentResponse.getHostname());
            serviceCallResponse.setVersion(testkomponentResponse.getVersion());
            htmlInfo.setServiceCallResponse(serviceCallResponse);
        }

        updateLastAccess();

        return htmlInfo;
    }

    private void updateLastAccess() {
        LastAccessed lastAccessed = new LastAccessed();
        lastAccessed.setAccessTime(LocalDateTime.now());

        lastAccessedDao.insert(lastAccessed);
    }
}
