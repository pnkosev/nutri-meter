package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FoodCreateBindingModel {

    @NotNull(message = "Food name is mandatory!")
    @Length(min = 3, max = 30, message = "Food name should be between 3 and 30 symbols!")
    private String name;

    @NotNull(message = "Description is mandatory!")
    @Length(min = 3, max = 100, message = "Description should be between 3 and 100 symbols!")
    private String description;

    private Integer kcalPerHundredGrams;

    @Range(min = 0, max = 100, message = "Proteins' level should be between 0 and 100!")
    private Double totalProteins;

    private Double cysteine;

    private Double histidine;

    private Double isoleucine;

    private Double leucine;

    private Double lysine;

    private Double methionine;

    private Double phenylalanine;

    private Double threonine;

    private Double tryptophan;

    private Double tyrosine;

    private Double valine;

    @Range(min = 0, max = 100, message = "Carbohydrates' level should be between 0 and 100!")
    private Double totalCarbohydrates;

    private Double fiber;

    private Double starch;

    private Double sugars;

    private Double addedSugars;

    @Range(min = 0, max = 100, message = "Lipids' level should be between 0 and 100!")
    private Double totalLipids;

    private Double monounsaturated;

    private Double polyunsaturated;

    private Double omega3;

    private Double omega6;

    private Double saturated;

    private Double transFats;

    private Double cholesterol;

    private Double vitaminA;

    private Double vitaminB1;

    private Double vitaminB2;

    private Double vitaminB3;

    private Double vitaminB5;

    private Double vitaminB6;

    private Double vitaminB12;

    private Double folate;

    private Double vitaminC;

    private Double vitaminD;

    private Double vitaminE;

    private Double vitaminK;

    private Double calcium;

    private Double copper;

    private Double iodine;

    private Double iron;

    private Double magnesium;

    private Double manganese;

    private Double phosphorus;

    private Double potassium;

    private Double selenium;

    private Double sodium;

    private Double zinc;

    @Size(min = 1, message = "You must choose at least 1 category!")
    private List<String> foodCategories;
}
