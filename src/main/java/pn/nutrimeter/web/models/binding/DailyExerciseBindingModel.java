package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DailyExerciseBindingModel {

    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NotBlank(message = "Duration is mandatory!")
    private String duration;

    @NotBlank(message = "KcalBurned are mandatory!")
    private String kcalBurned;

    @NotBlank(message = "Name is mandatory!")
    private String date;
}
