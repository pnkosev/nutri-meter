package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.FoodMineral;
import pn.nutrimeter.data.models.associations.FoodVitamin;
import pn.nutrimeter.data.models.associations.RecipeFood;

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

    @Column(name = "calories_per_hundred_grams", nullable = false)
    private Integer caloriesPerHundredGrams;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "food")
    private List<RecipeFood> recipeFoodAssociation;

    @OneToMany(mappedBy = "food")
    private List<FoodVitamin> foodVitaminAssociation;

    @OneToMany(mappedBy = "food")
    private List<FoodMineral> foodMineralAssociation;

//    @ManyToMany
//    @JoinTable(
//            name = "foods_ingredients",
//            joinColumns = @JoinColumn(name = "food_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
//    )
//    private List<Food> ingredients;
}
