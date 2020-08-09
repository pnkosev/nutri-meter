package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
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
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final String ROLE_ROOT = "ROLE_ROOT";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

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
            user.getAuthorities().add(this.roleRepository.findByAuthority(ROLE_USER));
        }

        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserRegisterServiceModel.class);
    }

    @Override
    public UserServiceModel getUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public MacroTargetServiceModel getMacroTargetByUserId(String userId, double userWeight) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.USER_ID_NOT_FOUND));
        MacroTarget macroTarget = user.getMacroTarget();

        return this.macroTargetServiceModelFactory.create(macroTarget, userWeight);
    }

    @Override
    public MicroTargetServiceModel getMicroTargetByUserId(String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.USER_ID_NOT_FOUND));
        return this.modelMapper.map(user.getMicroTarget(), MicroTargetServiceModel.class);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        Role roleRoot = roleRepository.findByAuthority("ROLE_ROOT");

        return this.userRepository
                .findAllByAuthoritiesNotContaining(roleRoot)
                .stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> getAllUsers(Specification<User> specification) {
        return this.userRepository.findAll(specification)
                .stream()
//                .filter(u -> {
//                    boolean isRoot = false;
//                    for (Role authority : u.getAuthorities()) {
//                        if (authority.getAuthority().equals(ROLE_ROOT)) {
//                            isRoot = true;
//                            break;
//                        }
//                    }
//                    return !isRoot;
//                })
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel promoteUser(String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException("No such user found!"));

        Set<Role> roles = user.getAuthorities();

        Set<String> rolesAsString = roles.stream().map(Role::getAuthority).collect(Collectors.toSet());

        if (!rolesAsString.contains(ROLE_ADMIN)) {
            roles.add(this.roleRepository.findByAuthority(ROLE_ADMIN));

            this.userRepository.saveAndFlush(user);
        }

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel demoteUser(String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException("No such user found!"));

        Set<Role> roles = user.getAuthorities();

        Set<String> rolesAsString = roles.stream().map(Role::getAuthority).collect(Collectors.toSet());

        if (rolesAsString.contains(ROLE_ADMIN)) {
            roles.clear();
            roles.add(this.roleRepository.findByAuthority(ROLE_USER));

            this.userRepository.saveAndFlush(user);
        }

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
    }

    private void seedRoles() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.saveAndFlush(new Role(ROLE_ROOT));
            this.roleRepository.saveAndFlush(new Role(ROLE_ADMIN));
            this.roleRepository.saveAndFlush(new Role(ROLE_USER));
        }
    }
}
