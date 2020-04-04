package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.service.factories.user.UserFactory;
import pn.nutrimeter.service.models.UserAuthenticatedServiceModel;
import pn.nutrimeter.service.models.UserLoginServiceModel;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.services.api.HashingService;
import pn.nutrimeter.service.services.api.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserFactory userFactory;

    private final HashingService hashingService;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, UserFactory userFactory, HashingService hashingService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.hashingService = hashingService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(UserRegisterServiceModel userRegisterServiceModel) {
        this.userRepository.saveAndFlush(this.userFactory.create(userRegisterServiceModel));
    }

    @Override
    public UserAuthenticatedServiceModel login(UserLoginServiceModel userLoginServiceModel) {
        String hashedPassword = this.hashingService.hash(userLoginServiceModel.getPassword());
        Optional<User> optionalUser = this.userRepository.findByUsernameAndPassword(userLoginServiceModel.getUsername(), hashedPassword);

        User user = new User();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        return this.modelMapper.map(user, UserAuthenticatedServiceModel.class);
    }
}
