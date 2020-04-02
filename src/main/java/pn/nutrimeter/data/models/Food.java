package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.DailyStoryFood;
import pn.nutrimeter.data.models.associations.RecipeFood;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "foods")
public class Food extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "kcal_per_hundred_grams", nullable = false)
    private Integer kcalPerHundredGrams;

    @Column(name = "total_proteins")
    private Double totalProteins;

    @Column(name = "cysteine")
    private Double cysteine;

    @Column(name = "histidine")
    private Double histidine;

    @Column(name = "isoleucine")
    private Double isoleucine;

    @Column(name = "leucine")
    private Double leucine;

    @Column(name = "lysine")
    private Double lysine;

    @Column(name = "methionine")
    private Double methionine;

    @Column(name = "phenylalanine")
    private Double phenylalanine;

    @Column(name = "threonine")
    private Double threonine;

    @Column(name = "tryptophan")
    private Double tryptophan;

    @Column(name = "tyrosine")
    private Double tyrosine;

    @Column(name = "valine")
    private Double valine;

    @Column(name = "total_carbohydrates")
    private Double totalCarbohydrates;

    @Column(name = "fiber")
    private Double fiber;

    @Column(name = "starch")
    private Double starch;

    @Column(name = "sugars")
    private Double sugars;

    @Column(name = "added_sugars")
    private Double addedSugars;

    @Column(name = "total_lipids")
    private Double totalLipids;

    @Column(name = "monounsaturated")
    private Double monounsaturated;

    @Column(name = "polyunsaturated")
    private Double polyunsaturated;

    @Column(name = "omega3")
    private Double omega3;

    @Column(name = "omega6")
    private Double omega6;

    @Column(name = "saturated")
    private Double saturated;

    @Column(name = "trans_fats")
    private Double transFats;

    @Column(name = "cholesterol")
    private Double cholesterol;

    @Column(name = "vitamin_a")
    private Double vitaminA;

    @Column(name = "vitamin_b1")
    private Double vitaminB1;

    @Column(name = "vitamin_b2")
    private Double vitaminB2;

    @Column(name = "vitamin_b3")
    private Double vitaminB3;

    @Column(name = "vitamin_b5")
    private Double vitaminB5;

    @Column(name = "vitamin_b6")
    private Double vitaminB6;

    @Column(name = "vitaminB12")
    private Double vitaminB12;

    @Column(name = "folate")
    private Double folate;

    @Column(name = "vitamin_c")
    private Double vitaminC;

    @Column(name = "vitamin_d")
    private Double vitaminD;

    @Column(name = "vitamin_e")
    private Double vitaminE;

    @Column(name = "vitamin_k")
    private Double vitaminK;

    @Column(name = "calcium")
    private Double calcium;

    @Column(name = "copper")
    private Double copper;

    @Column(name = "iodine")
    private Double iodine;

    @Column(name = "iron")
    private Double iron;

    @Column(name = "magnesium")
    private Double magnesium;

    @Column(name = "manganese")
    private Double manganese;

    @Column(name = "phosphorus")
    private Double phosphorus;

    @Column(name = "potassium")
    private Double potassium;

    @Column(name = "selenium")
    private Double selenium;

    @Column(name = "sodium")
    private Double sodium;

    @Column(name = "zinc")
    private Double zinc;

    @Column(name = "is_custom")
    private Double isCustom;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "food")
    private List<RecipeFood> recipeFoodAssociation;

    @OneToMany(mappedBy = "food")
    private List<DailyStoryFood> dailyStoryFoodAssociation;

    @ManyToMany(mappedBy = "favoriteFoods")
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "foods_food_categories",
            joinColumns = @JoinColumn(name = "food_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "food_category_id", referencedColumnName = "id")
    )
    private List<FoodCategory> foodCategories;

    @ManyToMany
    @JoinTable(
            name = "foods_measures",
            joinColumns = @JoinColumn(name = "food_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "measure_id", referencedColumnName = "id")
    )
    private List<Measure> measures;

//    @ManyToMany
//    @JoinTable(
//            name = "foods_ingredients",
//            joinColumns = @JoinColumn(name = "food_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
//    )
//    private List<Food> ingredients;
}
