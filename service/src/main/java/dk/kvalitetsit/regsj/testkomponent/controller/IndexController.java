package dk.kvalitetsit.regsj.testkomponent.controller;

import dk.kvalitetsit.regsj.testkomponent.service.HtmlService;
import dk.kvalitetsit.regsj.testkomponent.session.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.UnknownHostException;

@Controller
@RequestMapping("/html")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final HtmlService htmlService;

    public IndexController(HtmlService htmlService) {
        this.htmlService = htmlService;
    }

    @GetMapping
    @UserContext
    public ModelAndView get() throws UnknownHostException {
        logger.info("Request modtaget til html side.");

        var info = htmlService.getHtmlInfo();

        var modelAndView = new ModelAndView("index");
        modelAndView.addObject("version", info.getVersion());
        modelAndView.addObject("hostname", info.getHostName());
        modelAndView.addObject("configurableText", info.getConfigurableText());
        modelAndView.addObject("environment", info.getEnvironment());
        modelAndView.addObject("userContext", info.getUserContextInformation());

        info.getServiceCallResponse().ifPresent(x -> {
            modelAndView.addObject("remoteVersion", x.getVersion());
            modelAndView.addObject("remoteHostname", x.getHostname());
        });
        info.getServiceCallResponse().ifPresent(x -> modelAndView.addObject("remoteResponse", x.getContext()));
        info.getLastAccess().ifPresent(x -> modelAndView.addObject("lastAccess", x));

        return modelAndView;
    }
}
