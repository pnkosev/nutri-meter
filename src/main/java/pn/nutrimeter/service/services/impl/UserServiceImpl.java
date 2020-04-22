package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.data.models.Role;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.RoleRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.*;
import pn.nutrimeter.service.factories.macro_target.MacroTargetServiceModelFactory;
import pn.nutrimeter.service.factories.user.UserFactory;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.models.MicroTargetServiceModel;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.service.services.validation.UserValidationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserFactory userFactory;

    private final MacroTargetServiceModelFactory macroTargetServiceModelFactory;

    private final UserValidationService userValidationService;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository, UserFactory userFactory,
                           MacroTargetServiceModelFactory macroTargetServiceModelFactory,
                           UserValidationService userValidationService,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userFactory = userFactory;
        this.macroTargetServiceModelFactory = macroTargetServiceModelFactory;
        this.userValidationService = userValidationService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserRegisterServiceModel register(UserRegisterServiceModel userRegisterServiceModel) {
        if (!this.userValidationService.isNotNull(userRegisterServiceModel)) {
            throw new UserRegisterFailureException(ErrorConstants.USER_IS_NULL);
        }

        if (!this.userValidationService.arePasswordsMatching(userRegisterServiceModel)) {
            throw new UserRegisterFailureException(ErrorConstants.PASSWORDS_DO_NOT_MATCH);
        }

        if (!this.userValidationService.isUsernameFree(userRegisterServiceModel)) {
            throw new UserAlreadyExistsException(ErrorConstants.USERNAME_IS_TAKEN);
        }

        if (!this.userValidationService.isEmailFree(userRegisterServiceModel)) {
            throw new UserAlreadyExistsException(ErrorConstants.EMAIL_IS_TAKEN);
        }

        User user = this.userFactory.create(userRegisterServiceModel);

        this.seedRoles();
        if (this.userRepository.count() == 0) {
            user.setAuthorities(new HashSet<>(this.roleRepository.findAll()));
        } else {
            user.setAuthorities(new HashSet<>());
            user.getAuthorities().add(this.roleRepository.findByAuthority("USER"));
        }

        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserRegisterServiceModel.class);
    }

    @Override
    public UserServiceModel getUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public MacroTargetServiceModel getMacroTargetByUserId(String userId, double userWeight) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new IdNotFoundException(ErrorConstants.USER_ID_NOT_FOUND));
        MacroTarget macroTarget = user.getMacroTarget();
        MacroTargetServiceModel model = this.modelMapper.map(macroTarget, MacroTargetServiceModel.class);

        return this.macroTargetServiceModelFactory.create(macroTarget, model, userWeight);
    }

    @Override
    public MicroTargetServiceModel getMicroTargetByUserId(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new IdNotFoundException(ErrorConstants.USER_ID_NOT_FOUND));
        return this.modelMapper.map(user.getMicroTarget(), MicroTargetServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
    }

    private void seedRoles() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.saveAndFlush(new Role("ADMIN"));
            this.roleRepository.saveAndFlush(new Role("USER"));
        }
    }
}
