package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.error.UserAlreadyExistsException;
import pn.nutrimeter.error.UserRegisterFailureException;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.web.models.binding.UserRegisterBindingModel;

import javax.validation.Valid;
import java.util.Map;

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
            BindingResult bindingResult,
            Map<String, String> map) {

        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        try {
            this.userService.register(this.modelMapper.map(userRegisterBindingModel, UserRegisterServiceModel.class));
            return "redirect:/login";
        } catch (UserRegisterFailureException | UserAlreadyExistsException e) {
            map.put("reason", e.getMessage());
            return "user/register";
        }

    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
}
