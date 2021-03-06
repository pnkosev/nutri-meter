package pn.nutrimeter.service.services;

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
import pn.nutrimeter.data.repositories.MeasureRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.FoodAddFailureException;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.facades.AuthenticationFacade;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.service.services.impl.FoodServiceImpl;
import pn.nutrimeter.service.services.validation.FoodValidationService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class FoodServiceTest {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    MeasureRepository measureRepository;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    UserRepository userRepository;

    // CANNOT AUTOWIRE - AS IF THERE WAS NO @Service... BUT THERE IS ONE, I DON'T GET IT (Maybe cuz of the @DataJpaTest???)
    @MockBean
    FoodValidationService foodValidationService;

    ModelMapper modelMapper;

    FoodService foodService;

    FoodServiceModel foodServiceModel;

    @BeforeEach
    void setUp() {
        this.modelMapper = new ModelMapper();
        this.foodService = new FoodServiceImpl(userRepository, this.measureRepository, this.authenticationFacade, this.foodValidationService, this.foodRepository, this.modelMapper);
//        this.addFoodCategory();
    }

    @Test
    public void create_withValidModel_shouldReturnCorrect() {
        this.foodServiceModel = this.fillModel("food1");

        when(this.foodValidationService.isValid(this.foodServiceModel)).thenReturn(true);

        FoodServiceModel actual = this.foodService.create(this.foodServiceModel);
        FoodServiceModel expected = this.modelMapper.map(this.foodRepository.findAll().get(0), FoodServiceModel.class);

        assertEquals(expected.getId(), actual.getId());
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

        when(this.foodValidationService.isValid(this.foodServiceModel)).thenReturn(true);

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
        this.foodServiceModel.setIsCustom(false);
        FoodServiceModel food2 = this.fillModel("food2");
        food2.setKcalPerHundredGrams(100);
        food2.setIsCustom(true);
        FoodServiceModel food3 = this.fillModel("food3");
        food3.setKcalPerHundredGrams(100);
        food3.setIsCustom(true);

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
//        FoodCategoryServiceModel categoryServiceModel = this.modelMapper.map(this.foodCategoryRepository.findAll().get(0), FoodCategoryServiceModel.class);
//        List<FoodCategoryServiceModel> categories = new ArrayList<>();
//        categories.add(categoryServiceModel);
//        foodServiceModel.setFoodCategories(categories);

        return foodServiceModel;
    }

//    private void addFoodCategory() {
//        FoodCategory category = new FoodCategory();
//        category.setName("Fruits");
//        category.setDescription("yummy");
//        this.foodCategoryRepository.saveAndFlush(category);
//    }
}