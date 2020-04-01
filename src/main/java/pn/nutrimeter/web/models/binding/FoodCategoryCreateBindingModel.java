package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodCategoryCreateBindingModel {

    private String name;

    private String description;

    private Integer recommendedDailyServings;

    private Double amountInGrams;
}
