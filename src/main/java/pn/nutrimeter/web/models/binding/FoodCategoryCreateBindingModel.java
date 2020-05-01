package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class FoodCategoryCreateBindingModel {

    @NotNull
    @NotEmpty
    private String name;

    private String description;

    private Integer recommendedDailyServings;

    private Double amountInGrams;
}
