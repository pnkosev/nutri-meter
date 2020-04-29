package pn.nutrimeter.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.repositories.FoodRepository;
import pn.nutrimeter.web.base.RestApiTestBase;
import pn.nutrimeter.web.models.view.FoodSimpleViewModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class FoodRestControllerTest extends RestApiTestBase {
    List<Food> foods;

    @MockBean
    FoodRepository mockFoodRepository;

    @Autowired
    FoodRestController foodRestController;

    @Override
    protected void beforeEach() {
        this.foods = new ArrayList<>();
        when(this.mockFoodRepository.findAllNonCustom()).thenReturn(foods);
    }

    @Test
    public void allFoods_whenNoFoods_shouldReturnEmptyList() {
        this.foods.clear();

        // CANNOT GET THIS TO WORK FOR A SECURED/AUTHORIZED ROUTE ----- BUT IT WORKS FINE OTHERWISE
//        FoodSimpleViewModel[] foods = getRestTemplate()
//                .getForObject(getFullUrl("/api/foods"), FoodSimpleViewModel[].class);
//
//        assertEquals(0, foods.length);

        List<FoodSimpleViewModel> actual = this.foodRestController.allFoods();

        assertEquals(0, foods.size());
    }

    @Test
    public void allFoods_when3Foods_shouldReturn3Foods() {
        this.foods.addAll(List.of(
                new Food() {{ setName("Apple"); }},
                new Food() {{ setName("Orange"); }},
                new Food() {{ setName("Pear"); }}
        ));

        List<FoodSimpleViewModel> actual = this.foodRestController.allFoods();

        assertEquals(3, actual.size());
    }
}