package dk.kvalitetsit.regsj.testkomponent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.UnknownHostException;

@Controller
@RequestMapping("/")
public class RedirectController {

    @GetMapping
    public ModelAndView get() throws UnknownHostException {
        return new ModelAndView("redirect:/html/");
    }
}
