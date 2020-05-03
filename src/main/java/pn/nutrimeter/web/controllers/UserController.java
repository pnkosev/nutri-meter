package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.annotation.PageTitle;
import pn.nutrimeter.error.UserAlreadyExistsException;
import pn.nutrimeter.error.UserRegisterFailureException;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.web.models.binding.UserRegisterBindingModel;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
public class UserController extends BaseController {

    public static final String USER_REGISTER_URL = "/register";
    public static final String USER_REGISTER_VIEW = "user/register";
    public static final String USER_LOGIN_URL = "/login";
    public static final String USER_LOGIN_VIEW = "user/login";
    public static final String REDIRECT_URL = "/login";

    private final UserService userService;

    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PageTitle("Register")
    public ModelAndView register(UserRegisterBindingModel userRegisterBindingModel) {
        return view("user/register");
    }

    @PostMapping("/register")
    public ModelAndView registerPost(
            @Valid UserRegisterBindingModel userRegisterBindingModel,
            BindingResult bindingResult,
            Map<String, String> map) {

        if (bindingResult.hasErrors()) {
            return view("user/register", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            this.userService.register(this.modelMapper.map(userRegisterBindingModel, UserRegisterServiceModel.class));
        } catch (UserRegisterFailureException | UserAlreadyExistsException e) {
            map.put("reason", e.getMessage());
            return view("user/register", e.getHttpStatus());
        }

        return redirect("/login");
    }


    @GetMapping("/login")
    @PageTitle("Login")
    public ModelAndView login() {
        return view("user/login");
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @GetMapping("/user/all")
    @PageTitle("All Users")
    public ModelAndView allUsers() {
        return view("user/all-users");
    }
}
