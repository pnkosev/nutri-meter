package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class DailyStoryFoodServiceModel {

    private Timestamp timeOfDay;

    private FoodServiceModel foodServiceModel;
}
