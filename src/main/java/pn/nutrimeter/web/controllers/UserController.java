package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.service.models.UserAuthenticatedServiceModel;
import pn.nutrimeter.service.models.UserLoginServiceModel;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.web.models.binding.UserLoginBindingModel;
import pn.nutrimeter.web.models.binding.UserRegisterBindingModel;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    public ModelAndView register(UserRegisterBindingModel userRegisterBindingModel) {
        return new ModelAndView("user/register");
    }

    @PostMapping("/register")
    public String registerPost(
            @Valid UserRegisterBindingModel userRegisterBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        try {
            this.userService.register(this.modelMapper.map(userRegisterBindingModel, UserRegisterServiceModel.class));
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            return "user/register";
        }

    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login")
    public String loginPost(
            HttpSession session,
            @ModelAttribute UserLoginBindingModel userLoginBindingModel) {

        UserAuthenticatedServiceModel loggedInUser = this.userService.login(this.modelMapper.map(userLoginBindingModel, UserLoginServiceModel.class));

        if (loggedInUser.getUsername() == null) {
            return "user/login";
        }

        session.setAttribute("username", loggedInUser.getUsername());
        session.setAttribute("userId", loggedInUser.getUserId());
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/";
    }
}
