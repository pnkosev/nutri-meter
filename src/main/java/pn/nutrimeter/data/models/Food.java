package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.FoodMineral;
import pn.nutrimeter.data.models.associations.FoodVitamin;
import pn.nutrimeter.data.models.associations.RecipeFood;
import pn.nutrimeter.data.models.base.BaseEntity;
import pn.nutrimeter.data.models.macro.Carbohydrate;
import pn.nutrimeter.data.models.macro.Fat;
import pn.nutrimeter.data.models.macro.Protein;

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

    @Column(name = "kcal_per_hundred_grams", nullable = false)
    private Integer kcalPerHundredGrams;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @ManyToOne
    @JoinColumn(name = "fat_profile_id", referencedColumnName = "id")
    private Fat fats;

    @ManyToOne
    @JoinColumn(name = "carb_profile_id", referencedColumnName = "id")
    private Carbohydrate carbs;

    @ManyToOne
    @JoinColumn(name = "protein_profile_id", referencedColumnName = "id")
    private Protein proteins;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "food")
    private List<RecipeFood> recipeFoodAssociation;

    @OneToMany(mappedBy = "food")
    private List<FoodVitamin> foodVitaminAssociation;

    @OneToMany(mappedBy = "food")
    private List<FoodMineral> foodMineralAssociation;

    @ManyToMany
    @JoinTable(
            name = "foods_categories",
            joinColumns = @JoinColumn(name = "food_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private List<Category> categories;

//    @ManyToMany
//    @JoinTable(
//            name = "foods_ingredients",
//            joinColumns = @JoinColumn(name = "food_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
//    )
//    private List<Food> ingredients;
}
