package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyExerciseBindingModel {

    private String name;

    private Double duration;

    private Double kcalBurned;

    private String date;
}
