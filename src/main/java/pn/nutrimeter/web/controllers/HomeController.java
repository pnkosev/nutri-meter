package pn.nutrimeter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.annotation.PageTitle;

import java.security.Principal;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    @PageTitle("Index")
    public ModelAndView index() {
        return view("index");
    }

    @GetMapping("/home")
    @PageTitle("Home")
    public ModelAndView home() {
        return view("home");
    }
}
