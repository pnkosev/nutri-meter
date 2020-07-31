package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyStoryExerciseServiceModel {

    private String id;

    private String name;

    private Double kcalBurnedPerMin;

    private Double duration;
}
