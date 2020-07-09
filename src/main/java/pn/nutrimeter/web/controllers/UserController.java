package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/user")
public class UserController extends BaseController {

    public static final String USER_REGISTER_URL = "/register";
    public static final String USER_REGISTER_VIEW = "user/register";
    public static final String USER_LOGIN_URL = "/login";
    public static final String USER_LOGIN_VIEW = "user/login";
    public static final String ADMIN_TOOL_URL = "/admin-tool";
    public static final String ADMIN_TOOL_VIEW = "user/admin-tool";
    public static final String REDIRECT_URL = "/login";

    private final UserService userService;

    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(USER_REGISTER_URL)
    @PageTitle("Register")
    public ModelAndView register(UserRegisterBindingModel userRegisterBindingModel) {
        return view(USER_REGISTER_VIEW);
    }

    @PostMapping(USER_REGISTER_URL)
    public ModelAndView registerPost(
            @Valid UserRegisterBindingModel userRegisterBindingModel,
            BindingResult bindingResult,
            Map<String, String> map) {

        if (bindingResult.hasErrors()) {
            return view(USER_REGISTER_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            this.userService.register(this.modelMapper.map(userRegisterBindingModel, UserRegisterServiceModel.class));
        } catch (UserRegisterFailureException | UserAlreadyExistsException e) {
            map.put("reason", e.getMessage());
            return view(USER_REGISTER_VIEW, e.getHttpStatus());
        }

        return redirect(USER_LOGIN_URL);
    }


    @GetMapping(USER_LOGIN_URL)
    @PageTitle("Login")
    public ModelAndView login() {
        return view(USER_LOGIN_VIEW);
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @GetMapping(ADMIN_TOOL_URL)
    @PageTitle("Admin tool")
    public ModelAndView allUsers() {
        return view(ADMIN_TOOL_VIEW);
    }
}
