package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class FoodCategoryCreateBindingModel {

    @NotNull
    @NotEmpty(message = "Category name is mandatory!")
    @Length(min = 3, max = 30, message = "Category name should be between 3 and 30 symbols!")
    private String name;

    @NotNull
    @NotEmpty(message = "Category description is mandatory!")
    @Length(min = 10, max = 200, message = "Category description should be between 10 and 200 symbols!")
    private String description;
}
