package pn.nutrimeter.service.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.UserAlreadyExistsException;
import pn.nutrimeter.error.UserRegisterFailureException;
import pn.nutrimeter.service.factories.macro_target.MacroTargetServiceModelFactory;
import pn.nutrimeter.service.factories.user.UserFactory;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.services.api.RoleService;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.service.services.validation.UserValidationService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {

    private static final String PASSWORD = "pass";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";

    private UserRegisterServiceModel user;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserFactory userFactory;

    @MockBean
    MacroTargetServiceModelFactory macroTargetFactory;

    @MockBean
    UserValidationService userValidationService;

    @MockBean
    RoleService roleService;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        this.user = new UserRegisterServiceModel();
        this.user.setPassword(PASSWORD);
        this.user.setConfirmPassword(PASSWORD);
        this.user.setUsername(USERNAME);
        this.user.setEmail(EMAIL);
    }


    @Test
    public void register_withNullModel_shouldThrow() {
        this.user = null;

        assertThrows(UserRegisterFailureException.class, () -> this.userService.register(this.user));

        verify(this.userValidationService).isNotNull(this.user);
    }

    @Test
    public void register_withNotMatchingPasswords_shouldThrow() {
        this.user.setConfirmPassword("differentPass");

        // You have to mock the previous validations in order to reach it the current
        when(this.userValidationService.isNotNull(this.user)).thenReturn(true);

        assertThrows(UserRegisterFailureException.class, () -> this.userService.register(this.user));

        verify(this.userValidationService).arePasswordsMatching(this.user);
    }

    @Test
    public void register_withExistingUsername_shouldThrow() {
        // You have to mock the previous validations in order to reach it the current
        when(this.userValidationService.isNotNull(this.user)).thenReturn(true);
        when(this.userValidationService.arePasswordsMatching(this.user)).thenReturn(true);
        when(this.userValidationService.isUsernameFree(this.user)).thenReturn(false);

        assertThrows(UserAlreadyExistsException.class, () -> this.userService.register(this.user));

        verify(this.userValidationService).isUsernameFree(this.user);
    }

    @Test
    public void register_withExistingEmail_shouldThrow() {
        // You have to mock the previous validations in order to reach it the current
        when(this.userValidationService.isNotNull(this.user)).thenReturn(true);
        when(this.userValidationService.arePasswordsMatching(this.user)).thenReturn(true);
        when(this.userValidationService.isUsernameFree(this.user)).thenReturn(true);
        when(this.userValidationService.isEmailFree(this.user)).thenReturn(false);

        assertThrows(UserAlreadyExistsException.class, () -> this.userService.register(this.user));

        verify(this.userValidationService).isEmailFree(this.user);
    }


}