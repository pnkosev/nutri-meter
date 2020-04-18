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
    public boolean arePasswordsMatching(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    @Override
    public boolean isUsernameFree(String username) {
        return !this.userRepository.existsByUsername(username);
    }
}
