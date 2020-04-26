package pn.nutrimeter.service.services.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pn.nutrimeter.base.TestBase;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.models.associations.DailyStoryFood;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.UserNotFoundException;
import pn.nutrimeter.service.models.DailyStoryServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DailyStoryServiceImplTest extends TestBase {

    private static final LocalDate DATE = LocalDate.now();
    private static final String ID = "ID";
    private static final double WEIGHT = 100.0;

    @MockBean
    DailyStoryRepository dailyStoryRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    DailyStoryService service;

    @Test
    public void getByDateAndUserId_withNewDate_shouldReturnCorrect() {
        User user = mock(User.class);

        when(this.dailyStoryRepository.findByDateAndUserId(DATE, ID)).thenReturn(Optional.empty());
        when(this.userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(user.getWeight()).thenReturn(WEIGHT);

        DailyStoryServiceModel actual = this.service.getByDateAndUserId(DATE, ID);

        assertEquals(WEIGHT, actual.getDailyWeight());
        assertEquals(0, actual.getDailyStoryFoodAssociation().size());
        assertEquals(0.0, actual.getCalcium());
    }

    @Test
    public void getByDateAndUserId_withExistingDateTodayOrAfter_shouldReturnCorrect() {
        DailyStory dailyStory = this.getDailyStory();
        User user = mock(User.class);

        when(this.dailyStoryRepository.findByDateAndUserId(DATE, ID)).thenReturn(Optional.of(dailyStory));
        when(this.userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(user.getWeight()).thenReturn(WEIGHT);

        DailyStoryServiceModel actual = this.service.getByDateAndUserId(DATE, ID);

        assertEquals(WEIGHT, actual.getDailyWeight());
        assertEquals(3, actual.getDailyStoryFoodAssociation().size());
        assertEquals(300.0, actual.getVitaminA());
        assertEquals(3.0, actual.getTotalProteins());
        assertEquals(3.0, actual.getCalcium());
    }

    @Test
    public void getByDateAndUserId_withExistingDateBeforeToday_shouldReturnCorrect() {
        DailyStory dailyStory = this.getDailyStory();
        User user = mock(User.class);
        LocalDate yesterday = LocalDate.from(DATE).minusDays(1);

        when(this.dailyStoryRepository.findByDateAndUserId(yesterday, ID)).thenReturn(Optional.of(dailyStory));
        when(this.userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(user.getWeight()).thenReturn(WEIGHT);

        DailyStoryServiceModel actual = this.service.getByDateAndUserId(yesterday, ID);

        assertNull(actual.getDailyWeight());
        assertEquals(3, actual.getDailyStoryFoodAssociation().size());
        assertEquals(300.0, actual.getVitaminA());
        assertEquals(3.0, actual.getTotalProteins());
        assertEquals(3.0, actual.getCalcium());
    }

    @Test
    public void getByDateAndUserId_withNonExistingUserId_shouldThrow() {
        assertThrows(UserNotFoundException.class, () -> this.service.getByDateAndUserId(DATE, ID));
    }

    private DailyStory getDailyStory() {
        DailyStory dailyStory = new DailyStory();

        List<DailyStoryFood> dailyStoryFoodList = new ArrayList<>();
        dailyStoryFoodList.add(this.getDailyStoryFood());
        dailyStoryFoodList.add(this.getDailyStoryFood());
        dailyStoryFoodList.add(this.getDailyStoryFood());

        dailyStory.setDailyStoryFoodAssociation(dailyStoryFoodList);

        return dailyStory;
    }

    private DailyStoryFood getDailyStoryFood() {
        DailyStoryFood dailyStoryFood = new DailyStoryFood();

        dailyStoryFood.setGramsConsumed(100.0);
        dailyStoryFood.setFood(this.getFood());

        return dailyStoryFood;
    }

    private Food getFood() {
        Food food = new Food();

        food.setName("Apple");
        food.setKcalPerHundredGrams(100);
        food.setTotalProteins(1.0);
        food.setVitaminA(100.0);
        food.setCalcium(1.0);

        return food;
    }
}