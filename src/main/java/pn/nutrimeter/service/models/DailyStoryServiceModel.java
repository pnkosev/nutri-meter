package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DailyStoryServiceModel {

    private LocalDate date;

    private List<DailyStoryFoodServiceModel> dailyStoryFoodAssociation;
}
