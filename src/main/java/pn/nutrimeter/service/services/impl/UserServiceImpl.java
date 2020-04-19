package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Role;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.UserAlreadyExistsException;
import pn.nutrimeter.error.UserNotFoundException;
import pn.nutrimeter.error.UserRegisterFailureException;
import pn.nutrimeter.service.factories.user.UserFactory;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;
import pn.nutrimeter.service.services.api.RoleService;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.service.services.validation.UserValidationService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserFactory userFactory;

    private final UserValidationService userValidationService;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, UserFactory userFactory, UserValidationService userValidationService, RoleService roleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.userValidationService = userValidationService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void register(UserRegisterServiceModel userRegisterServiceModel) {
        if (!this.userValidationService.isNotNull(userRegisterServiceModel)) {
            throw new UserRegisterFailureException(ErrorConstants.USER_IS_NULL);
        }

        if (!this.userValidationService.isUsernameFree(userRegisterServiceModel.getUsername())) {
            throw new UserAlreadyExistsException(ErrorConstants.USERNAME_IS_TAKEN);
        }

        if (!this.userValidationService.isEmailFree(userRegisterServiceModel.getEmail())) {
            throw new UserAlreadyExistsException(ErrorConstants.EMAIL_IS_TAKEN);
        }

        if (!this.userValidationService.arePasswordsMatching(userRegisterServiceModel.getPassword(), userRegisterServiceModel.getConfirmPassword())) {
            throw new UserRegisterFailureException(ErrorConstants.PASSWORDS_DO_NOT_MATCH);
        }


        this.roleService.seedRoles();
        User user = this.userFactory.create(userRegisterServiceModel);
        if (this.userRepository.count() == 0) {
            Set<Role> authorities = this.roleService.getAllAuthority()
                    .stream()
                    .map(a -> this.modelMapper.map(a, Role.class))
                    .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        } else {
            user.setAuthorities(new HashSet<>());
            user.getAuthorities().add(this.modelMapper.map(this.roleService.getByAuthority("USER"), Role.class));
        }
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public UserServiceModel getUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
    }
}
