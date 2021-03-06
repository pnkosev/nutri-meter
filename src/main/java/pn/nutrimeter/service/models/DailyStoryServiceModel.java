package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.DailyStoryExercise;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DailyStoryServiceModel {

    private String id;

    private LocalDate date;

    private Double dailyWeight;

    private List<DailyStoryFoodServiceModel> dailyStoryFoodAssociation;

    private List<DailyStoryExerciseServiceModel> dailyStoryExerciseAssociation;

    private Double kcalConsumed;

    private Double kcalBurned;

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

    private Double totalCarbohydrates;

    private Double fiber;

    private Double starch;

    private Double sugars;

    private Double addedSugars;

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
}
