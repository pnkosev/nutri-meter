package pn.nutrimeter.service.services.api;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.models.MicroTargetServiceModel;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserRegisterServiceModel register(UserRegisterServiceModel userServiceModel);

    UserServiceModel getUserByUsername(String username);

    MacroTargetServiceModel getMacroTargetByUserId(String userId, double userWeight);

    MicroTargetServiceModel getMicroTargetByUserId(String userId);

    List<UserServiceModel> getAllUsers();

    List<UserServiceModel> getAllUsers(Specification<User> specification);

    UserServiceModel promoteUser(String userId);

    UserServiceModel demoteUser(String userId);
}
