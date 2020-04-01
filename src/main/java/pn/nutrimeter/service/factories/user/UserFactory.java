package pn.nutrimeter.service.factories.user;

import pn.nutrimeter.data.models.User;
import pn.nutrimeter.service.models.UserRegisterServiceModel;

public interface UserFactory {

    User create(UserRegisterServiceModel userRegisterServiceModel);
}
