package pn.nutrimeter.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import pn.nutrimeter.data.models.LifeStageGroup;
import pn.nutrimeter.data.models.enums.Sex;
import pn.nutrimeter.data.repositories.LifeStageGroupRepository;
import pn.nutrimeter.web.base.MvcTestBase;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TargetControllerTest extends MvcTestBase {

    private static final String BASE_TARGET_URL = "/target";

    @MockBean
    LifeStageGroupRepository mockLifeStageGroupRepository;

    List<LifeStageGroup> models;

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAdd_whenLoggedIn_shouldReturnCorrect() throws Exception {
        this.mockMvc.perform(get(BASE_TARGET_URL + TargetController.LIFE_STAGE_GROUP_ADD_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(TargetController.LIFE_STAGE_GROUP_ADD_VIEW));
    }

    @Test
    public void lifeStageGroupAdd_whenNotLoggedIn_shouldRedirectToLogin() throws Exception {
        this.mockMvc.perform(get(BASE_TARGET_URL + TargetController.LIFE_STAGE_GROUP_ADD_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAddPost_whenBindingResultHasNoErrors_shouldReturnCorrectAndRedirectToHome() throws Exception {
        this.mockMvc
                .perform(post(BASE_TARGET_URL + TargetController.LIFE_STAGE_GROUP_ADD_URL)
                        .param("sex", String.valueOf(Sex.FEMALE))
                        .param("lowerAgeBound", "10")
                        .param("upperAgeBound", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TargetController.HOME_URL_REDIRECT));
    }

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAddPost_whenBindingResultHasErrors_shouldResultInInputError() throws Exception {
        this.mockMvc
                .perform(post(BASE_TARGET_URL + TargetController.LIFE_STAGE_GROUP_ADD_URL))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(view().name(TargetController.LIFE_STAGE_GROUP_ADD_VIEW));
    }

    @Test
    @WithMockUser("test")
    public void macroTargetAdd_whenLoggedIn_shouldReturnCorrect() throws Exception {
        this.models = List.of(
                this.getLifeStageGroup(),
                this.getLifeStageGroup()
        );

        when(this.mockLifeStageGroupRepository.findAll()).thenReturn(this.models);

        this.mockMvc
                .perform(get(BASE_TARGET_URL + TargetController.MACRO_TARGET_ADD_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(TargetController.MACRO_TARGET_ADD_VIEW));
    }

    @Test
    public void macroTargetAdd_whenNotLoggedIn_shouldRedirectToLogin() throws Exception {
        this.models = List.of(
                this.getLifeStageGroup(),
                this.getLifeStageGroup()
        );

        when(this.mockLifeStageGroupRepository.findAll()).thenReturn(this.models);

        this.mockMvc
                .perform(get(BASE_TARGET_URL + TargetController.MACRO_TARGET_ADD_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser("test")
    public void macroTargetAdd_whenBindingResultHasNoErrors_shouldReturnCorrectAndRedirectToHome() throws Exception {
        this.mockMvc
                .perform(post(BASE_TARGET_URL + TargetController.MACRO_TARGET_ADD_URL)
                    .param("proteinRDA", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TargetController.HOME_URL_REDIRECT));
    }

    @Test
    @WithMockUser("test")
    public void macroTargetAdd_whenBindingResultHasErrors_shouldResultInInputError() throws Exception {
        this.mockMvc
                .perform(post(BASE_TARGET_URL + TargetController.MACRO_TARGET_ADD_URL))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(view().name(TargetController.MACRO_TARGET_ADD_VIEW));
    }

    @Test
    @WithMockUser("test")
    public void microTargetAdd_whenLoggedIn_shouldReturnCorrect() throws Exception {
        this.models = List.of(
                this.getLifeStageGroup(),
                this.getLifeStageGroup()
        );

        when(this.mockLifeStageGroupRepository.findAll()).thenReturn(this.models);

        this.mockMvc
                .perform(get(BASE_TARGET_URL + TargetController.MICRO_TARGET_ADD_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(TargetController.MICRO_TARGET_ADD_VIEW));
    }

    @Test
    public void microTargetAdd_whenNotLoggedIn_shouldRedirectToLogin() throws Exception {
        this.models = List.of(
                this.getLifeStageGroup(),
                this.getLifeStageGroup()
        );

        when(this.mockLifeStageGroupRepository.findAll()).thenReturn(this.models);

        this.mockMvc
                .perform(get(BASE_TARGET_URL + TargetController.MICRO_TARGET_ADD_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser("test")
    public void microTargetAdd_whenBindingResultHasNoErrors_shouldReturnCorrectAndRedirectToHome() throws Exception {
        this.mockMvc
                .perform(post(BASE_TARGET_URL + TargetController.MICRO_TARGET_ADD_URL)
                        .param("vitaminARDA", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TargetController.HOME_URL_REDIRECT));
    }

    @Test
    @WithMockUser("test")
    public void microTargetAdd_whenBindingResultHasErrors_shouldResultInInputError() throws Exception {
        this.mockMvc
                .perform(post(BASE_TARGET_URL + TargetController.MICRO_TARGET_ADD_URL))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(view().name(TargetController.MICRO_TARGET_ADD_VIEW));
    }

    private LifeStageGroup getLifeStageGroup() {
        return new LifeStageGroup() {{
            setId("ID");
            setSex(Sex.MALE);
            setLowerAgeBound(5.0);
            setUpperAgeBound(15.0);
        }};
    }
}