package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.RecipeFood;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "total_weight_in_grams")
    private Double totalWeight;

    @Column(name = "total_kcal")
    private Double totalKcal;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeFood> recipeFoodAssociation;

    @ManyToMany
    @JoinTable(
            name = "recipes_food_category",
            joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "food_category_id", referencedColumnName = "id")
    )
    private List<FoodCategory> foodCategories;
}
