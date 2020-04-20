package pn.nutrimeter.service.services.validation;

import pn.nutrimeter.service.models.UserRegisterServiceModel;

public interface UserValidationService {

    boolean isNotNull(UserRegisterServiceModel user);

    boolean isUsernameFree(UserRegisterServiceModel user);

    boolean isEmailFree(UserRegisterServiceModel user);

    boolean arePasswordsMatching(UserRegisterServiceModel user);
}
