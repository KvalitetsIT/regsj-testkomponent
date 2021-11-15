package dk.kvalitetsit.hello.configuration;

import dk.kvalitetsit.hello.service.HelloService;
import dk.kvalitetsit.hello.service.HelloServiceImpl;
import dk.kvalitetsit.hello.service.HtmlService;
import dk.kvalitetsit.hello.service.HtmlServiceImpl;
import dk.kvalitetsit.prometheus.app.info.actuator.VersionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class HelloConfiguration {
    @Bean
    public HelloService helloService() {
        return new HelloServiceImpl();
    }

    @Bean
    public HtmlService htmlService(VersionProvider versionProvider,@Value("${CONFIGURABLE_TEXT}") String configurableText, @Value("${ENVIRONMENT}") String environment) {
        return new HtmlServiceImpl(versionProvider, configurableText, environment);
    }

//    @Bean
//    @RequestScope
//    public UserContextService userContextService(HttpServletRequest request,
//                                                 @Value("${usercontext.header.name}") String userContextHeaderName,
//                                                 @Value("${userattributes.org.key}") String userAttributeOrgKey) {
//        return new UserContextServiceImpl(request, userContextHeaderName, userAttributeOrgKey);
//    }

}
