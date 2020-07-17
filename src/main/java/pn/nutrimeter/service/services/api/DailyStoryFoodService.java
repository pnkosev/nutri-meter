package pn.nutrimeter.service.services.api;

import java.time.LocalDate;

public interface DailyStoryFoodService {

    void create(String measure, Double equivalentInGrams, double quantity, LocalDate date, String foodId, String userId);
}
