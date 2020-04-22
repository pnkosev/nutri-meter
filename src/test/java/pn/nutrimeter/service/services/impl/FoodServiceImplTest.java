package pn.nutrimeter.service.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.nutrimeter.data.models.FoodCategory;
import pn.nutrimeter.data.repositories.FoodCategoryRepository;
import pn.nutrimeter.data.repositories.FoodRepository;
import pn.nutrimeter.error.FoodAddFailureException;
import pn.nutrimeter.service.facades.AuthenticationFacade;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.service.services.validation.FoodValidationService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class FoodServiceImplTest {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    FoodCategoryRepository foodCategoryRepository;

    // CANNOT AUTOWIRE - AS IF NO THERE WAS NO @Service... BUT THERE IS ONE, I DON'T GET IT
    @MockBean
    FoodValidationService foodValidationService;

    @MockBean
    AuthenticationFacade authenticationFacade;

    ModelMapper modelMapper;

    FoodService foodService;

    FoodServiceModel foodServiceModel;

    @BeforeEach
    void setUp() {
        this.modelMapper = new ModelMapper();
        this.foodService = new FoodServiceImpl(this.foodValidationService, this.foodRepository, this.foodCategoryRepository, this.authenticationFacade, this.modelMapper);
        this.addFoodCategory();
        this.fillModel();
    }

    @Test
    public void create_withValidModelIfAdmin_shouldReturnCorrect() {
        this.fillModel();
        Set<String> roles = new HashSet<>(Arrays.asList("ADMIN", "USER"));

        when(this.foodValidationService.isValid(this.foodServiceModel)).thenReturn(true);
        when(this.authenticationFacade.getRoles()).thenReturn(roles);

        FoodServiceModel actual = this.foodService.create(this.foodServiceModel);
        FoodServiceModel expected = this.modelMapper.map(this.foodRepository.findAll().get(0), FoodServiceModel.class);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.isCustom(), actual.isCustom());
        assertFalse(actual.isCustom());
        assertEquals(expected.getKcalPerHundredGrams(), actual.getKcalPerHundredGrams());
    }

    @Test
    public void create_withValidModelIfNotAdmin_shouldReturnCorrect() {
        this.fillModel();
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        when(this.foodValidationService.isValid(this.foodServiceModel)).thenReturn(true);
        when(this.authenticationFacade.getRoles()).thenReturn(roles);

        FoodServiceModel actual = this.foodService.create(this.foodServiceModel);
        FoodServiceModel expected = this.modelMapper.map(this.foodRepository.findAll().get(0), FoodServiceModel.class);

        assertEquals(expected.getId(), actual.getId());
        assertTrue(actual.isCustom());
        assertEquals(expected.isCustom(), actual.isCustom());
        assertEquals(expected.getKcalPerHundredGrams(), actual.getKcalPerHundredGrams());
    }

    @Test
    public void create_withInvalidInput_shouldThrow() {
        when(this.foodValidationService.isValid(this.foodServiceModel)).thenReturn(false);
        assertThrows(FoodAddFailureException.class, () -> this.foodService.create(this.foodServiceModel));
    }

    private void fillModel() {
        this.foodServiceModel = new FoodServiceModel();

        this.foodServiceModel.setName("testFood");
        this.foodServiceModel.setTotalProteins(1d);
        this.foodServiceModel.setDescription("yummy");
        this.foodServiceModel.setTotalCarbohydrates(1d);
        this.foodServiceModel.setTotalLipids(1d);
        FoodCategoryServiceModel categoryServiceModel = this.modelMapper.map(this.foodCategoryRepository.findAll().get(0), FoodCategoryServiceModel.class);
        List<FoodCategoryServiceModel> categories = new ArrayList<>();
        categories.add(categoryServiceModel);
        this.foodServiceModel.setFoodCategories(categories);
    }

    private void addFoodCategory() {
        FoodCategory category = new FoodCategory();
        category.setName("Fruits");
        category.setDescription("yummy");
        category.setRecommendedDailyServings(5);
        category.setAmountInGrams(500d);
        this.foodCategoryRepository.saveAndFlush(category);
    }
}