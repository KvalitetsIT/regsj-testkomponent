package dk.kvalitetsit.hello.controller;

import dk.kvalitetsit.hello.service.HtmlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.UnknownHostException;

@Controller
@RequestMapping("/html")
public class IndexController {
    private final HtmlService htmlService;

    public IndexController(HtmlService htmlService) {
        this.htmlService = htmlService;
    }

    @GetMapping
    public ModelAndView get() throws UnknownHostException {
        var info = htmlService.getHtmlInfo();

        var modelAndView = new ModelAndView("index");
        modelAndView.addObject("version", info.getVersion());
        modelAndView.addObject("hostname", info.getHostName());

        return modelAndView;
    }
}
