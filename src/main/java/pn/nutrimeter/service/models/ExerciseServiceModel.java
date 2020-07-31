package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseServiceModel {

    private String id;

    private String name;

    private String category;

    private Double kcalBurnedPerHour;
}
