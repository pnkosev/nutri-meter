package pn.nutrimeter.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.FoodCategory;
import pn.nutrimeter.data.repositories.FoodCategoryRepository;
import pn.nutrimeter.data.repositories.FoodRepository;
import pn.nutrimeter.web.base.MvcTestBase;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FoodControllerTest extends MvcTestBase {

    private static final String BASE_URL = "/food";

    @MockBean
    FoodCategoryRepository mockFoodCategoryRepository;

    @MockBean
    FoodRepository mockFoodRepository;

    @Test
    @WithMockUser
    public void addFood_whenAuthenticated_shouldReturnCorrect() throws Exception {
        List<FoodCategory> foodCategories = List.of(
                this.getFoodCategory(),
                this.getFoodCategory()
        );

        when(this.mockFoodCategoryRepository.findAll()).thenReturn(foodCategories);

        this.mockMvc
                .perform(get(BASE_URL + FoodController.FOOD_ADD_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(FoodController.FOOD_ADD_VIEW));
    }

    @Test
    public void addFood_whenAnonymous_shouldRedirectToLogin() throws Exception {
        this.mockMvc
                .perform(get(BASE_URL + FoodController.FOOD_ADD_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void addFoodPost_whenBindingResultHasNoErrors_shouldReturnCorrectAndRedirect() throws Exception {
        when(this.mockFoodCategoryRepository.findById(any())).thenReturn(Optional.of(new FoodCategory()));
        when(this.mockFoodRepository.saveAndFlush(any())).thenReturn(new Food());

        this.mockMvc
                .perform(post(BASE_URL + FoodController.FOOD_ADD_URL)
                        .param("name", "name")
                        .param("description", "description")
                        .param("totalProteins", "5")
                        .param("totalCarbohydrates", "5")
                        .param("totalLipids", "5")
                        .param("foodCategories", this.getFoodCategoryString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(FoodController.REDIRECT_HOME_URL));
    }

    @Test
    @WithMockUser
    public void addFoodPost_whenBindingResultHasErrors_shouldReturnCorrectAndRedirect() throws Exception {
        this.mockMvc
                .perform(post(BASE_URL + FoodController.FOOD_ADD_URL)
                        .param("name", "n")
                        .param("description", "description")
                        .param("totalProteins", "5")
                        .param("totalCarbohydrates", "5")
                        .param("totalLipids", "5")
                        .param("foodCategories", this.getFoodCategoryString()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(view().name(FoodController.FOOD_ADD_VIEW));
    }

    @Test
    @WithMockUser
    public void addCategory_whenAuthenticated_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(get(BASE_URL + FoodController.FOOD_CATEGORY_ADD_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(FoodController.FOOD_CATEGORY_ADD_EDIT_VIEW));
    }

    @Test
    public void addCategory_whenAnonymous_shouldRedirectToLogin() throws Exception {
        this.mockMvc
                .perform(get(BASE_URL + FoodController.FOOD_CATEGORY_ADD_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void addCategoryPost_whenBindingResultHasNoErrors_shouldReturnCorrectAndRedirect() throws Exception {
        this.mockMvc
                .perform(post(BASE_URL + FoodController.FOOD_CATEGORY_ADD_URL)
                        .param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(FoodController.REDIRECT_HOME_URL));
    }

    @Test
    @WithMockUser
    public void addCategoryPost_whenBindingResultHasErrors_shouldResultInInputError() throws Exception {
        this.mockMvc
                .perform(post(BASE_URL + FoodController.FOOD_CATEGORY_ADD_URL))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(view().name(FoodController.FOOD_CATEGORY_ADD_EDIT_VIEW));
    }

    private FoodCategory getFoodCategory() { return new FoodCategory() {{ setName("NAME"); }}; }

    private String getFoodCategoryString() { return "ID"; }
}