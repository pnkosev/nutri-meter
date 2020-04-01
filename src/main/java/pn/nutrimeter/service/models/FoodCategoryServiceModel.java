package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodCategoryServiceModel {

    private String id;

    private String name;

    private String description;

    private Integer recommendedDailyServings;

    private Double amountInGrams;
}
