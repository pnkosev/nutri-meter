package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyStoryFoodServiceModel {

    private String id;

    private Double gramsConsumed;

    private FoodServiceModel food;

    private DailyStoryServiceModel dailyStory;
}
