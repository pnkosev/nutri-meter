package pn.nutrimeter.service.services.api;

import org.springframework.security.core.userdetails.UserDetailsService;
import pn.nutrimeter.service.models.UserAuthenticatedServiceModel;
import pn.nutrimeter.service.models.UserLoginServiceModel;
import pn.nutrimeter.service.models.UserRegisterServiceModel;

public interface UserService extends UserDetailsService {

    void create(UserRegisterServiceModel userServiceModel);

    UserAuthenticatedServiceModel login(UserLoginServiceModel userLoginServiceModel);
}
