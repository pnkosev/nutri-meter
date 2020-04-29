package pn.nutrimeter.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import pn.nutrimeter.data.models.enums.Sex;
import pn.nutrimeter.data.repositories.LifeStageGroupRepository;
import pn.nutrimeter.web.base.MvcTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TargetControllerTest extends MvcTestBase {

    @MockBean
    LifeStageGroupRepository mockLifeStageGroupRepository;

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAdd_whenLoggedIn_shouldReturnCorrect() throws Exception {
        this.mockMvc.perform(get("/target/group/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("target/life-stage-group-add"));
    }

    @Test
    public void lifeStageGroupAdd_whenNotLoggedIn_shouldReturnCorrect() throws Exception {
        this.mockMvc.perform(get("/target/group/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAddPost_whenBindingResultHasNoErrors_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(
                        post("/target/group/add")
                                .param("sex", String.valueOf(Sex.FEMALE))
                                .param("lowerAgeBound", "10")
                                .param("upperAgeBound", "20"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAddPost_whenBindingResultHasErrors_shouldResultInError() throws Exception {
        this.mockMvc
                .perform(
                        post("/target/group/add")
                                .param("yo", "yo"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(view().name("target/life-stage-group-add"));
    }
}