package pn.nutrimeter.service.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.nutrimeter.data.models.Role;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.RoleRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.UserAlreadyExistsException;
import pn.nutrimeter.error.UserRegisterFailureException;
import pn.nutrimeter.service.factories.macro_target.MacroTargetServiceModelFactory;
import pn.nutrimeter.service.factories.user.UserFactory;
import pn.nutrimeter.service.models.RoleServiceModel;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.service.services.validation.UserValidationService;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {

    private static final String USERNAME = "username";

    private UserRegisterServiceModel user;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    UserFactory userFactory;

    @MockBean
    MacroTargetServiceModelFactory macroTargetFactory;

    @MockBean
    UserValidationService userValidationService;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        this.user = mock(UserRegisterServiceModel.class);

        when(this.userValidationService.isNotNull(this.user)).thenReturn(true);
        when(this.userValidationService.arePasswordsMatching(this.user)).thenReturn(true);
        when(this.userValidationService.isUsernameFree(this.user)).thenReturn(true);
        when(this.userValidationService.isEmailFree(this.user)).thenReturn(true);
    }

    @Test
    public void register_withNullModel_shouldThrow() {
        this.user = null;

        assertThrows(UserRegisterFailureException.class, () -> this.userService.register(this.user));

        verify(this.userValidationService).isNotNull(this.user);
    }

    @Test
    public void register_withNotMatchingPasswords_shouldThrow() {
        when(this.userValidationService.arePasswordsMatching(this.user)).thenReturn(false);

        assertThrows(UserRegisterFailureException.class, () -> this.userService.register(this.user));

        verify(this.userValidationService).arePasswordsMatching(this.user);
    }

    @Test
    public void register_withExistingUsername_shouldThrow() {
        when(this.userValidationService.isUsernameFree(this.user)).thenReturn(false);

        assertThrows(UserAlreadyExistsException.class, () -> this.userService.register(this.user));

        verify(this.userValidationService).isUsernameFree(this.user);
    }

    @Test
    public void register_withExistingEmail_shouldThrow() {
        when(this.userValidationService.isEmailFree(this.user)).thenReturn(false);

        assertThrows(UserAlreadyExistsException.class, () -> this.userService.register(this.user));

        verify(this.userValidationService).isEmailFree(this.user);
    }

    @Test
    public void register_firstValidUser_shouldReturnCorrect() {
        List<Role> allRoles = new ArrayList<>(Arrays.asList(new Role("ADMIN"), new Role("USER")));

        User user = this.getUser();
        user.setAuthorities(new HashSet<>(allRoles));

        when(this.userFactory.create(this.user)).thenReturn(user);
        when(this.userRepository.count()).thenReturn(0L);
        when(this.roleRepository.findAll()).thenReturn(allRoles);
        when(this.userRepository.saveAndFlush(user)).thenReturn(user);

        UserRegisterServiceModel actual = this.userService.register(this.user);
        List<String> actualAuthorities = actual.getAuthorities().stream().map(RoleServiceModel::getAuthority).collect(Collectors.toList());

        assertEquals(USERNAME, user.getUsername());
        assertEquals(2, actual.getAuthorities().size());
        assertTrue(actualAuthorities.contains("ADMIN"));
    }

    @Test
    public void register_normalUser_shouldReturnCorrect() {
        Role userRole = new Role("USER");

        User user = this.getUser();
        user.setAuthorities(new HashSet<>());
        user.getAuthorities().add(userRole);

        when(this.userFactory.create(this.user)).thenReturn(user);
        when(this.userRepository.count()).thenReturn(1L);
        when(this.roleRepository.findByAuthority("USER")).thenReturn(userRole);
        when(this.userRepository.saveAndFlush(user)).thenReturn(user);

        UserRegisterServiceModel actual = this.userService.register(this.user);

        assertEquals(USERNAME, user.getUsername());
        assertEquals(1, actual.getAuthorities().size());
        assertEquals("USER", new ArrayList<>(actual.getAuthorities()).get(0).getAuthority());
    }

    private User getUser() {
        User user = new User();
        user.setUsername(USERNAME);
        return user;
    }
}