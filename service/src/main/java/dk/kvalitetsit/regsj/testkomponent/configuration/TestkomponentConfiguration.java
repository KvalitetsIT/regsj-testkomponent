package dk.kvalitetsit.regsj.testkomponent.configuration;

import dk.kvalitetsit.regsj.testkomponent.service.RestService;
import dk.kvalitetsit.regsj.testkomponent.service.RestServiceImpl;
import dk.kvalitetsit.regsj.testkomponent.service.HtmlService;
import dk.kvalitetsit.regsj.testkomponent.service.HtmlServiceImpl;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextService;
import dk.kvalitetsit.regsj.testkomponent.session.UserContextServiceImpl;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebMvc
public class TestkomponentConfiguration {
    @Bean
    public RestService helloService(UserContextService userContextService) {
        return new RestServiceImpl(userContextService);
    }

    @Bean
    public HtmlService htmlService(VersionProvider versionProvider, @Value("${CONFIGURABLE_TEXT}") String configurableText, @Value("${ENVIRONMENT}") String environment, UserContextService userContextService) {
        return new HtmlServiceImpl(versionProvider, configurableText, environment, userContextService);
    }

    @Bean
    @RequestScope
    public UserContextService userContextService(HttpServletRequest request,
                                                 @Value("${usercontext.header.name}") String userContextHeaderName) {
        return new UserContextServiceImpl(request, userContextHeaderName);
    }
}
