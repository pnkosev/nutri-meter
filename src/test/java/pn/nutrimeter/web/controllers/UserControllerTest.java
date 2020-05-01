package pn.nutrimeter.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import pn.nutrimeter.data.models.*;
import pn.nutrimeter.data.models.enums.ActivityLevel;
import pn.nutrimeter.data.models.enums.Sex;
import pn.nutrimeter.data.repositories.*;
import pn.nutrimeter.web.base.MvcTestBase;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends MvcTestBase {

    @MockBean
    UserRepository mockUserRepository;

    @MockBean
    RoleRepository mockRoleRepository;

    @MockBean
    LifeStageGroupRepository mockLifeStageGroupRepository;

    @MockBean
    MacroTargetRepository mockMacroTargetRepository;

    @MockBean
    MicroTargetRepository mockMicroTargetRepository;

    @Test
    public void register_withAnonymousUser_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(get(UserController.USER_REGISTER_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.USER_REGISTER_VIEW));
    }

    @Test
    @WithMockUser("test")
    public void register_withAuthenticatedUser_shouldReturnError() throws Exception {
        this.mockMvc
                .perform(get(UserController.USER_REGISTER_URL))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void register_whenBindingResultHasNoErrors_shouldReturnCorrectAndRedirect() throws Exception {
        Role role = new Role("USER");
        User user = new User();
        LifeStageGroup lifeStageGroup = mock(LifeStageGroup.class);
        MacroTarget macroTarget = mock(MacroTarget.class);
        MicroTarget microTarget = mock(MicroTarget.class);
        when(this.mockUserRepository.count()).thenReturn(1L);
        when(this.mockUserRepository.saveAndFlush(any())).thenReturn(user);
        when(this.mockRoleRepository.findByAuthority(any())).thenReturn(role);
        when(this.mockLifeStageGroupRepository.findLifeStageGroupBySexAndAge(Sex.MALE, 0.0)).thenReturn(Optional.of(lifeStageGroup));
        when(this.mockMacroTargetRepository.findByLifeStageGroupId(any())).thenReturn(Optional.of(macroTarget));
        when(this.mockMicroTargetRepository.findByLifeStageGroupId(any())).thenReturn(Optional.of(microTarget));

        this.mockMvc
                .perform(post(UserController.USER_REGISTER_URL)
                        .param("username", "username")
                        .param("email", "test@test.com")
                        .param("password", "Password1")
                        .param("confirmPassword", "Password1")
                        .param("sex", Sex.MALE.toString())
                        .param("weight", "70")
                        .param("height", "185")
                        .param("birthday", this.getYesterday().toString())
                        .param("activityLevel", ActivityLevel.LIGHT.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(UserController.REDIRECT_URL));
    }

    @Test
    public void register_whenBindingResultHasErrors_shouldResultInInputError() throws Exception {
        this.mockMvc
                .perform(post(UserController.USER_REGISTER_URL)
                        .param("username", "u")
                        .param("email", "test@test.com")
                        .param("password", "Password1")
                        .param("confirmPassword", "Password1")
                        .param("sex", Sex.MALE.toString())
                        .param("weight", "70")
                        .param("height", "185")
                        .param("birthday", this.getYesterday().toString())
                        .param("activityLevel", ActivityLevel.LIGHT.toString()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(view().name(UserController.USER_REGISTER_VIEW));
    }

    @Test
    public void register_whenPasswordsDoNotMatch_shouldResultInInputError() throws Exception {
        this.mockMvc
                .perform(post(UserController.USER_REGISTER_URL)
                        .param("username", "username")
                        .param("email", "test@test.com")
                        .param("password", "Password1")
                        .param("confirmPassword", "Password0")
                        .param("sex", Sex.MALE.toString())
                        .param("weight", "70")
                        .param("height", "185")
                        .param("birthday", this.getYesterday().toString())
                        .param("activityLevel", ActivityLevel.LIGHT.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(view().name(UserController.USER_REGISTER_VIEW));
    }

    @Test
    public void register_whenUsernameIsNotFree_shouldResultInInputError() throws Exception {
        when(this.mockUserRepository.existsByUsername(any())).thenReturn(true);

        this.mockMvc
                .perform(post(UserController.USER_REGISTER_URL)
                        .param("username", "username")
                        .param("email", "test@test.com")
                        .param("password", "Password1")
                        .param("confirmPassword", "Password1")
                        .param("sex", Sex.MALE.toString())
                        .param("weight", "70")
                        .param("height", "185")
                        .param("birthday", this.getYesterday().toString())
                        .param("activityLevel", ActivityLevel.LIGHT.toString()))
                .andExpect(status().isConflict())
                .andExpect(view().name(UserController.USER_REGISTER_VIEW));
    }

    @Test
    public void register_whenEmailIsNotFree_shouldResultInInputError() throws Exception {
        when(this.mockUserRepository.existsByEmail(any())).thenReturn(true);

        this.mockMvc
                .perform(post(UserController.USER_REGISTER_URL)
                        .param("username", "username")
                        .param("email", "test@test.com")
                        .param("password", "Password1")
                        .param("confirmPassword", "Password1")
                        .param("sex", Sex.MALE.toString())
                        .param("weight", "70")
                        .param("height", "185")
                        .param("birthday", this.getYesterday().toString())
                        .param("activityLevel", ActivityLevel.LIGHT.toString()))
                .andExpect(status().isConflict())
                .andExpect(view().name(UserController.USER_REGISTER_VIEW));
    }

    @Test
    public void login_whenAnonymous_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(get(UserController.USER_LOGIN_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.USER_LOGIN_VIEW));
    }

    private LocalDate getYesterday() {
        LocalDate now = LocalDate.now();
        return now.minusDays(1);
    }
}