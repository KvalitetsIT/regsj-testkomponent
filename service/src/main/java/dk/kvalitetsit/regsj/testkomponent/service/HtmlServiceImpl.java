package dk.kvalitetsit.regsj.testkomponent.service;
import java.time.OffsetDateTime;

import dk.kvalitetsit.regsj.testkomponent.dao.LastAccessedDao;
import dk.kvalitetsit.regsj.testkomponent.dao.entity.LastAccessed;
import dk.kvalitetsit.regsj.testkomponent.remote.TestkomponentClient;
import dk.kvalitetsit.regsj.testkomponent.service.model.HtmlInfo;
import dk.kvalitetsit.regsj.testkomponent.service.model.ServiceCallResponse;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import dk.medcom.audit.client.AuditClient;
import dk.medcom.audit.client.api.v1.AuditEvent;
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
    private final AuditClient auditClient;

    public HtmlServiceImpl(VersionProvider VersionProvider,
                           String configurableText,
                           String environment,
                           UserContextService userContextService,
                           boolean doServiceCall,
                           TestkomponentClient testkomponentClient,
                           LastAccessedDao lastAccessedDao, AuditClient auditClient) {
        this.VersionProvider = VersionProvider;
        this.configurableText = configurableText;
        this.environment = environment;
        this.userContextService = userContextService;
        this.doServiceCall = doServiceCall;
        this.testkomponentClient = testkomponentClient;
        this.lastAccessedDao = lastAccessedDao;
        this.auditClient = auditClient;
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
            var testkomponentResponseProtected = testkomponentClient.callTestClientProtected();

            var serviceCallResponse = new ServiceCallResponse();
            serviceCallResponse.setHostname(testkomponentResponse.getHostname());
            serviceCallResponse.setVersion(testkomponentResponse.getVersion());
            serviceCallResponse.setContext(new HashMap<>());
            testkomponentResponseProtected.getContext().forEach( context -> serviceCallResponse.getContext().put(context.getAttributeName().replace('.', '_'), String.join(", ", context.getAttributeValue())));

            htmlInfo.setServiceCallResponse(serviceCallResponse);

        }

        updateLastAccess();

        addAuditLog(htmlInfo);

        return htmlInfo;
    }

    private void addAuditLog(HtmlInfo htmlInfo) {
        var auditEvent = new AuditEvent<HtmlInfo>();

        auditEvent.setAuditData(htmlInfo);
        auditEvent.setOrganisationCode("some_org");
        auditEvent.setOperation("get");
        auditEvent.setAuditEventDateTime(OffsetDateTime.now());
        auditEvent.setSource("testkomponent-a");
        auditEvent.setIdentifier("");
        auditEvent.setUser("some_user");
        auditEvent.setResource("html");

        auditClient.addAuditEntry(auditEvent);
    }

    private void updateLastAccess() {
        LastAccessed lastAccessed = new LastAccessed();
        lastAccessed.setAccessTime(LocalDateTime.now());

        lastAccessedDao.insert(lastAccessed);
    }
}
