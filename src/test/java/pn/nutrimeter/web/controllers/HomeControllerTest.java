package pn.nutrimeter.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pn.nutrimeter.web.base.ViewTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest extends ViewTestBase {

    @Test
    public void index_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(
                        get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser("test")
    public void home_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(
                        get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }
}