package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Role;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.errors.ErrorConstants;
import pn.nutrimeter.errors.UserNotFoundException;
import pn.nutrimeter.service.factories.user.UserFactory;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;
import pn.nutrimeter.service.services.api.RoleService;
import pn.nutrimeter.service.services.api.UserService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserFactory userFactory;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, UserFactory userFactory, RoleService roleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void register(UserRegisterServiceModel userRegisterServiceModel) {
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
