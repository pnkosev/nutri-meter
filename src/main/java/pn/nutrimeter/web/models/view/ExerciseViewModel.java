package pn.nutrimeter.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseViewModel {

    private String id;

    private String name;

    private Double kcalBurnedPerHour;
}
