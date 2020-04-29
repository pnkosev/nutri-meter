package pn.nutrimeter.service.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pn.nutrimeter.base.TestBase;
import pn.nutrimeter.data.models.FoodCategory;
import pn.nutrimeter.data.repositories.FoodCategoryRepository;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.services.api.FoodCategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodCategoryServiceTest extends TestBase {

    @MockBean
    FoodCategoryRepository repository;

    @Autowired
    FoodCategoryService service;

    FoodCategoryServiceModel model;
    FoodCategory foodCategory;

    @Override
    protected void beforeEach() {
        this.model = getModel();
        this.foodCategory = getFoodCategory();
    }

    @Test
    public void create_withValidModel_shouldReturnCorrect() {
        this.service.create(this.model);

        ArgumentCaptor<FoodCategory> argument = ArgumentCaptor.forClass(FoodCategory.class);
        verify(this.repository).saveAndFlush(argument.capture());

        FoodCategory foodCategory = argument.getValue();
        assertNotNull(foodCategory);
    }

    @Test
    public void create_withNullModel_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> this.service.create(null));
    }

    @Test
    public void getById_withExistingId_shouldReturnCorrect() {
        when(this.repository.findById(this.foodCategory.getId())).thenReturn(Optional.of(this.foodCategory));

        FoodCategoryServiceModel actual = this.service.getById(this.foodCategory.getId());

        verify(this.repository).findById(this.foodCategory.getId());
        assertEquals(this.foodCategory.getId(), actual.getId());
        assertEquals(this.foodCategory.getName(), actual.getName());
        assertEquals(this.foodCategory.getDescription(), actual.getDescription());
    }

    @Test
    public void getById_withNonExistingId_shouldThrow() {
        assertThrows(IdNotFoundException.class, () -> this.service.getById("123"));
    }

    @Test
    public void getAll_shouldReturnCorrect() {
        List<FoodCategory> foodCategories = new ArrayList<>();
        foodCategories.add(this.foodCategory);

        when(this.repository.findAll()).thenReturn(foodCategories);

        List<FoodCategoryServiceModel> actual = this.service.getAll();

        verify(this.repository).findAll();
        assertEquals(foodCategories.size(), actual.size());
        assertEquals(foodCategories.get(0).getId(), actual.get(0).getId());
        assertEquals(foodCategories.get(0).getName(), actual.get(0).getName());
        assertEquals(foodCategories.get(0).getDescription(), actual.get(0).getDescription());
    }

    private FoodCategoryServiceModel getModel() {
        FoodCategoryServiceModel foodCategory = new FoodCategoryServiceModel();
        foodCategory.setId("123");
        foodCategory.setName("name");
        foodCategory.setDescription("description");
        return foodCategory;
    }

    private FoodCategory getFoodCategory() {
        FoodCategory foodCategory = new FoodCategory();
        foodCategory.setId("123");
        foodCategory.setName("name");
        foodCategory.setDescription("description");
        return foodCategory;
    }
}