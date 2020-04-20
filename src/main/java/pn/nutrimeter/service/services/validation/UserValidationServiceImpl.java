package pn.nutrimeter.service.services.validation;

import org.springframework.stereotype.Service;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.service.models.UserRegisterServiceModel;

@Service
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;

    public UserValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isNotNull(UserRegisterServiceModel user) {
        return user != null;
    }

    @Override
    public boolean arePasswordsMatching(UserRegisterServiceModel user) {
        return user.getPassword().equals(user.getConfirmPassword());
    }

    @Override
    public boolean isUsernameFree(UserRegisterServiceModel user) {
        return !this.userRepository.existsByUsername(user.getUsername());
    }

    @Override
    public boolean isEmailFree(UserRegisterServiceModel user) { return !this.userRepository.existsByEmail(user.getEmail()); }
}
