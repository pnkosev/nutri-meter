package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class DailyExerciseBindingModel {

    @NotBlank(message = "Name is mandatory!")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters!")
    private String name;

    @NotBlank(message = "Duration is mandatory!")
    @Size(min = 1, max = 3, message = "Duration must be expressed in between 1 and 3 digits!")
    private String duration;

    @NotBlank(message = "KcalBurned are mandatory!")
    @Size(min = 1, max = 4, message = "Kcal burned must be expressed in between 1 and 4 digits!")
    private String kcalBurned;

    @NotBlank(message = "Name is mandatory!")
    private String date;
}
