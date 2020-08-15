package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class MeasureCreateBindingModel {

    @NotBlank(message = "Measure name is mandatory!")
    @Size(min = 1, max = 10, message = "Name must be between 1 and 10 characters!")
    private String name;

    @NotBlank(message = "Equivalent in grams is mandatory!")
    @Size(min = 1, max = 4, message = "Equivalent in grams must be expressed in between 1 and 4 digits!")
    private String equivalentInGrams;
}
