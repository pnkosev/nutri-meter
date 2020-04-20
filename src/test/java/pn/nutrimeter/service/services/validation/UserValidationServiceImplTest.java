package pn.nutrimeter.service.services.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.service.models.UserRegisterServiceModel;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserValidationServiceImplTest {

    private static final String PASSWORD = "pass";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";

    UserRegisterServiceModel user;

    @MockBean
    UserRepository repository;

    @Autowired
    UserValidationService service;

    @BeforeEach
    void setUp() {
        this.user = new UserRegisterServiceModel();
        this.user.setPassword(PASSWORD);
        this.user.setConfirmPassword(PASSWORD);
        this.user.setUsername(USERNAME);
        this.user.setEmail(EMAIL);
    }

    @Test
    public void isNotNull_withNotNullInput_shouldReturnTrue() {
        UserRegisterServiceModel user = new UserRegisterServiceModel();

        boolean isValid = this.service.isNotNull(user);

        assertTrue(isValid);
    }

    @Test
    public void isNotNull_withNullInput_shouldReturnFalse() {
        this.user = null;

        boolean isValid = this.service.isNotNull(user);

        assertFalse(isValid);
    }

    @Test
    public void arePasswordsMatching_withMatchingPasswords_shouldReturnTrue() {
        boolean isValid = this.service.arePasswordsMatching(user);

        assertTrue(isValid);
    }

    @Test
    public void arePasswordsMatching_withNotMatchingPasswords_shouldReturnFalse() {
        this.user.setConfirmPassword(PASSWORD + "1");

        boolean isValid = this.service.arePasswordsMatching(user);

        assertFalse(isValid);
    }

    @Test
    public void isUsernameFree_withFreeUsername_shouldReturnTrue() {
        when(this.repository.existsByUsername(user.getUsername())).thenReturn(false);

        boolean isValid = this.service.isUsernameFree(user);

        verify(this.repository).existsByUsername(user.getUsername());

        assertTrue(isValid);
    }

    @Test
    public void isUsernameFree_withExistingUsername_shouldReturnFalse() {
        when(this.repository.existsByUsername(user.getUsername())).thenReturn(true);

        boolean isValid = this.service.isUsernameFree(user);

        verify(this.repository).existsByUsername(user.getUsername());

        assertFalse(isValid);
    }

    @Test
    public void isEmailFree_withFreeEmail_shouldReturnTrue() {
        when(this.repository.existsByEmail(user.getEmail())).thenReturn(false);

        boolean isValid = this.service.isEmailFree(user);

        verify(this.repository).existsByEmail(user.getEmail());

        assertTrue(isValid);
    }

    @Test
    public void isEmailFree_withExistingEmail_shouldReturnFalse() {
        when(this.repository.existsByEmail(user.getEmail())).thenReturn(true);

        boolean isValid = this.service.isEmailFree(user);

        verify(this.repository).existsByEmail(user.getEmail());

        assertFalse(isValid);
    }
}