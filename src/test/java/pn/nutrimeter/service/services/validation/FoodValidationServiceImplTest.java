package pn.nutrimeter.service.services.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FoodValidationServiceImplTest {

    @Autowired
    FoodValidationService foodValidationService;

    FoodServiceModel foodServiceModel;

    @Test
    public void isValid_withValidModel_shouldReturnTrue() {
        this.foodServiceModel = new FoodServiceModel();
        this.foodServiceModel.setName("name");
        this.foodServiceModel.setDescription("description");
        List<FoodCategoryServiceModel> categories = new ArrayList<>();
        categories.add(new FoodCategoryServiceModel());
        this.foodServiceModel.setFoodCategories(categories);
        assertTrue(this.foodValidationService.isValid(this.foodServiceModel));
    }

    @Test
    public void isValid_withNullModel_shouldReturnFalse() {
        assertFalse(this.foodValidationService.isValid(this.foodServiceModel));
    }

    @Test
    public void isValid_withFoodNameNullOrEmpty_shouldReturnFalse() {
        this.foodServiceModel = new FoodServiceModel();
        assertFalse(this.foodValidationService.isValid(this.foodServiceModel));
    }

    @Test
    public void isValid_withFoodDescriptionNullOrEmpty_shouldReturnFalse() {
        this.foodServiceModel = new FoodServiceModel();
        this.foodServiceModel.setName("name");
        assertFalse(this.foodValidationService.isValid(this.foodServiceModel));
    }

    @Test
    public void isValid_withFoodCategoryEmpty_shouldReturnFalse() {
        this.foodServiceModel = new FoodServiceModel();
        this.foodServiceModel.setName("name");
        this.foodServiceModel.setDescription("description");
        this.foodServiceModel.setFoodCategories(new ArrayList<>());
        assertFalse(this.foodValidationService.isValid(this.foodServiceModel));
    }
}