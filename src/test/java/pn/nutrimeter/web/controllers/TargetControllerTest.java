package pn.nutrimeter.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pn.nutrimeter.service.services.api.LifeStageGroupService;
import pn.nutrimeter.service.services.api.MacroTargetService;
import pn.nutrimeter.service.services.api.MicroTargetService;
import pn.nutrimeter.web.base.ViewTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TargetControllerTest extends ViewTestBase {

    @MockBean
    LifeStageGroupService lifeStageGroupService;

    @MockBean
    MacroTargetService macroTargetService;

    @MockBean
    MicroTargetService microTargetService;

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAdd_shouldReturnCorrect() throws Exception {
        this.mockMvc.perform(get("/target/group/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("target/life-stage-group-add"));
    }


}