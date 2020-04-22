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
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.FoodCategory;
import pn.nutrimeter.data.repositories.FoodCategoryRepository;
import pn.nutrimeter.data.repositories.FoodRepository;
import pn.nutrimeter.error.FoodAddFailureException;
import pn.nutrimeter.error.IdNotFoundException;
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

    // CANNOT AUTOWIRE - AS IF NO THERE WAS NO @Service... BUT THERE IS ONE, I DON'T GET IT (Maybe cuz of the @DataJpaTest???)
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
    }

    @Test
    public void create_withValidModelIfAdmin_shouldReturnCorrect() {
        this.foodServiceModel = this.fillModel("food1");
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
        this.foodServiceModel = this.fillModel("food1");
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

    @Test
    public void create_withNonExistingFoodCategory_shouldThrow() {
        this.foodServiceModel = this.fillModel("food1");
        FoodCategoryServiceModel foodCategoryServiceModel = new FoodCategoryServiceModel();
        foodCategoryServiceModel.setId("123");
        this.foodServiceModel.setFoodCategories(new ArrayList<>(Arrays.asList(foodCategoryServiceModel)));
        Set<String> roles = new HashSet<>(Arrays.asList("ADMIN", "USER"));

        when(this.foodValidationService.isValid(this.foodServiceModel)).thenReturn(true);
        when(this.authenticationFacade.getRoles()).thenReturn(roles);

        assertThrows(IdNotFoundException.class, () -> this.foodService.create(this.foodServiceModel));
    }

    @Test
    public void getAll_withExistingFoodsInDB_shouldReturnAll() {
        this.foodServiceModel = this.fillModel("food1");
        this.foodServiceModel.setKcalPerHundredGrams(100);
        FoodServiceModel food2 = this.fillModel("food2");
        food2.setKcalPerHundredGrams(100);
        FoodServiceModel food3 = this.fillModel("food3");
        food3.setKcalPerHundredGrams(100);

        List<FoodServiceModel> expected = new ArrayList<>(Arrays.asList(foodServiceModel, food2, food3));
        expected.forEach(f -> this.foodRepository.save(this.modelMapper.map(f, Food.class)));

        List<FoodServiceModel> actual = this.foodService.getAll();

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(1).getName(), actual.get(1).getName());
        assertEquals(expected.get(2).getName(), actual.get(2).getName());
    }

    @Test
    public void getAll_withEmptyDB_shouldReturnZero() {
        assertEquals(0, this.foodService.getAll().size());
    }

    @Test
    public void getAllNonCustom__withExistingNonCustomFoodsInDB_shouldReturnAll() {
        this.foodServiceModel = this.fillModel("food1");
        this.foodServiceModel.setKcalPerHundredGrams(100);
        this.foodServiceModel.setCustom(false);
        FoodServiceModel food2 = this.fillModel("food2");
        food2.setKcalPerHundredGrams(100);
        food2.setCustom(true);
        FoodServiceModel food3 = this.fillModel("food3");
        food3.setKcalPerHundredGrams(100);
        food3.setCustom(true);

        List<FoodServiceModel> expected = new ArrayList<>(Arrays.asList(foodServiceModel, food2, food3));
        expected.forEach(f -> this.foodRepository.save(this.modelMapper.map(f, Food.class)));

        List<FoodServiceModel> actual = this.foodService.getAllNonCustom();

        assertEquals(1, actual.size());
        assertEquals(this.foodServiceModel.getName(), actual.get(0).getName());
    }

    @Test
    public void getAllNonCustom_withEmptyDB_shouldReturnZero() {
        assertEquals(0, this.foodService.getAllNonCustom().size());
    }

    @Test
    public void getById_withValidId_shouldReturnCorrect() {
        this.foodServiceModel = this.fillModel("food");
        this.foodServiceModel.setKcalPerHundredGrams(100);

        Food food = this.foodRepository.save(this.modelMapper.map(this.foodServiceModel, Food.class));

        FoodServiceModel expected = this.modelMapper.map(food, FoodServiceModel.class);
        FoodServiceModel actual = this.foodService.getById(food.getId());

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    public void getById_withInvalidId_shouldThrow() {
        assertThrows(IdNotFoundException.class, () -> this.foodService.getById("123"));
    }

    private FoodServiceModel fillModel(String name) {
        FoodServiceModel foodServiceModel = new FoodServiceModel();

        foodServiceModel.setName(name);
        foodServiceModel.setDescription("yummy");
        foodServiceModel.setTotalProteins(1d);
        foodServiceModel.setTotalCarbohydrates(1d);
        foodServiceModel.setTotalLipids(1d);
        FoodCategoryServiceModel categoryServiceModel = this.modelMapper.map(this.foodCategoryRepository.findAll().get(0), FoodCategoryServiceModel.class);
        List<FoodCategoryServiceModel> categories = new ArrayList<>();
        categories.add(categoryServiceModel);
        foodServiceModel.setFoodCategories(categories);

        return foodServiceModel;
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