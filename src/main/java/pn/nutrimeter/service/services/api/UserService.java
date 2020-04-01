package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.UserAuthenticatedServiceModel;
import pn.nutrimeter.service.models.UserLoginServiceModel;
import pn.nutrimeter.service.models.UserRegisterServiceModel;

public interface UserService {

    void create(UserRegisterServiceModel userServiceModel);

    UserAuthenticatedServiceModel login(UserLoginServiceModel userLoginServiceModel);
}
