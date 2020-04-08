package pn.nutrimeter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public ModelAndView home(Principal principal) {
        ModelAndView mov = new ModelAndView();
        mov.addObject("name", principal.getName());
        mov.setViewName("home");
        return mov;
    }
}
