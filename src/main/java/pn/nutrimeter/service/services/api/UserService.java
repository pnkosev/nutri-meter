package pn.nutrimeter.service.services.api;

import org.springframework.security.core.userdetails.UserDetailsService;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.models.MicroTargetServiceModel;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;

public interface UserService extends UserDetailsService {

    void register(UserRegisterServiceModel userServiceModel);

    UserServiceModel getUserByUsername(String username);

    MacroTargetServiceModel getMacroTargetByUserId(String userId);

    MicroTargetServiceModel getMicroTargetByUserId(String userId);
}
