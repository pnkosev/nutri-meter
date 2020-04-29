package pn.nutrimeter.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pn.nutrimeter.web.base.MvcTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest extends MvcTestBase {

    @Test
    public void index_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(
                        get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andReturn();
    }

    @Test
    @WithMockUser("test")
    public void home_withLoggedInUser_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andReturn();
    }

    @Test
    public void home_ifNotLoggedIn_shouldShowErrorView() throws Exception {
        this.mockMvc
                .perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
                .andReturn();
    }
}