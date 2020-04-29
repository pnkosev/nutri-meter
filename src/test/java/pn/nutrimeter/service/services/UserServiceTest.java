package pn.nutrimeter.service.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pn.nutrimeter.base.TestBase;
import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.data.models.MicroTarget;
import pn.nutrimeter.data.models.Role;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.RoleRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.error.UserAlreadyExistsException;
import pn.nutrimeter.error.UserNotFoundException;
import pn.nutrimeter.error.UserRegisterFailureException;
import pn.nutrimeter.service.factories.macro_target.MacroTargetServiceModelFactory;
import pn.nutrimeter.service.factories.user.UserFactory;
import pn.nutrimeter.service.models.*;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.service.services.validation.UserValidationService;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest extends TestBase {

    private static final String USERNAME = "username";
    private static final String ID = "123";

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

    @Override
    protected void beforeEach() {
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

    @Test
    public void getUserByUsername_withExistingUsername_shouldReturnCorrect() {
        User user = this.getUser();

        when(this.userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        UserServiceModel actual = this.userService.getUserByUsername(USERNAME);

        assertEquals(USERNAME, actual.getUsername());
    }

    @Test
    public void getUserByUsername_withNonExistingUsername_shouldThrow() {
        assertThrows(UserNotFoundException.class, () -> this.userService.getUserByUsername(USERNAME));
    }

    @Test
    public void getMacroTargetByUserId_withExistingUserId_shouldReturnCorrect() {
        User user = this.getUser();
        MacroTarget macroTarget = mock(MacroTarget.class);
        user.setMacroTarget(macroTarget);
        MacroTargetServiceModel model = mock(MacroTargetServiceModel.class);

        when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(this.macroTargetFactory.create(macroTarget, 100.0)).thenReturn(model);

        MacroTargetServiceModel actual = this.userService.getMacroTargetByUserId(user.getId(), 100.0);

        verify(this.userRepository).findById(user.getId());
        verify(this.macroTargetFactory).create(macroTarget, 100.0);
        assertEquals(model, actual);
    }

    @Test
    public void getMacroTargetByUserId_withNonExistingUserId_shouldThrow() {
        assertThrows(IdNotFoundException.class, () -> this.userService.getMacroTargetByUserId(ID, 100.0));
    }

    @Test
    public void getMicroTargetByUserId_withExistingUserId_shouldReturnCorrect() {
        User user = this.getUser();
        MicroTarget microTarget = new MicroTarget();
        microTarget.setCalciumRDA(1000.0);
        microTarget.setCopperRDA(100.0);
        microTarget.setFolateRDA(100.0);
        user.setMicroTarget(microTarget);

        when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        MicroTargetServiceModel actual = this.userService.getMicroTargetByUserId(user.getId());

        verify(this.userRepository).findById(user.getId());
        assertEquals(microTarget.getCalciumRDA(), actual.getCalciumRDA());
        assertEquals(microTarget.getCopperRDA(), actual.getCopperRDA());
        assertEquals(microTarget.getFolateRDA(), actual.getFolateRDA());
    }

    @Test
    public void getMicroTargetByUserId_withNonExistingUserId_shouldThrow() {
        assertThrows(IdNotFoundException.class, () -> this.userService.getMicroTargetByUserId(ID));
    }

    @Test
    public void loadUserByUsername_withExistingUsername_shouldReturnCorrect() {
        User user = getUser();

        when(this.userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        UserDetails actual = this.userService.loadUserByUsername(USERNAME);

        verify(this.userRepository).findByUsername(USERNAME);
        assertEquals(user.getUsername(), actual.getUsername());
    }

    @Test
    public void loadUserByUsername_withNonExistingUsername_shouldThrow() {
        assertThrows(UsernameNotFoundException.class, () -> this.userService.loadUserByUsername(USERNAME));
    }

    private User getUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setId(ID);
        return user;
    }
}