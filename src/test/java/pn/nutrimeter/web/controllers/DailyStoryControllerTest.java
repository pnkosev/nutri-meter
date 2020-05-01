package pn.nutrimeter.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.data.models.MicroTarget;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.web.base.MvcTestBase;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DailyStoryControllerTest extends MvcTestBase {

    @MockBean
    UserRepository mockUserRepository;

    @MockBean
    DailyStoryRepository mockDailyStoryRepository;

    @Test
    @WithMockUser
    public void dailyStoryDefault_whenAuthenticated_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(get("/diary"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/diary/" + this.getToday()));
    }

    @Test
    public void dailyStoryDefault_whenAnonymous_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(get("/diary"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void dailyStory_whenAuthenticatedAndValidDate_shouldReturnCorrect() throws Exception {
        User user = new User() {{
            setId("ID");
            setMicroTarget(new MicroTarget());
        }};
        user.setMacroTarget(this.getMacroTarget());
        DailyStory dailyStory = mock(DailyStory.class);

        when(dailyStory.getDailyWeight()).thenReturn(10.0);
        when(this.mockDailyStoryRepository.findByDateAndUserId(this.getToday(), user.getId())).thenReturn(Optional.of(dailyStory));
        when(this.mockUserRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(this.mockUserRepository.findById(any())).thenReturn(Optional.of(user));

        this.mockMvc
                .perform(get("/diary/" + this.getToday()))
                .andExpect(status().isOk())
                .andExpect(view().name("diary/daily-story"));
    }

    @Test
    @WithMockUser
    public void dailyStory_whenAuthenticatedAndInvalidDate_shouldReturnErrorPage() throws Exception {
        this.mockMvc
                .perform(get("/diary/" + "asdasd"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"));
    }

    @Test
    public void dailyStory_whenAnonymous_shouldRedirectToLogin() throws Exception {
        this.mockMvc
                .perform(get("/diary/" + this.getToday()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    private LocalDate getToday() {
        return LocalDate.now();
    }

    private MacroTarget getMacroTarget() throws IllegalAccessException {
        MacroTarget macroTarget = new MacroTarget();

        Field[] declaredFields = MacroTarget.class.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType().equals(Double.class)) {
                field.setAccessible(true);
                field.set(macroTarget, 0.0);
            }
        }

        return macroTarget;
    }
}