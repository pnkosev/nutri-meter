package pn.nutrimeter.service.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FoodCategoryServiceImplTest {

    @MockBean
    FoodCategoryRepository repository;

    @Autowired
    FoodCategoryService service;

    FoodCategoryServiceModel model;
    FoodCategory foodCategory;

    @BeforeEach
    void setUp() {
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