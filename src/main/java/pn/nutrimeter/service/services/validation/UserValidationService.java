package pn.nutrimeter.service.services.validation;

import pn.nutrimeter.service.models.UserRegisterServiceModel;

public interface UserValidationService {

    boolean isNotNull(UserRegisterServiceModel user);

    boolean isUsernameFree(String username);

    boolean arePasswordsMatching(String password, String confirmPassword);
}
