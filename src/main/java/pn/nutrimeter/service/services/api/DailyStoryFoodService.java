package pn.nutrimeter.service.services.api;

import java.time.LocalDate;

public interface DailyStoryFoodService {

    void create(double quantity, LocalDate date, String foodId, String userId);
}
