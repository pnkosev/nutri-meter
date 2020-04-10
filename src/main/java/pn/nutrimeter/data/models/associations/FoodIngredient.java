package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "foods_ingredients")
@IdClass(FoodIngredientId.class)
public class FoodIngredient {

    @Id
    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @Id
    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private Food ingredient;

    @Column(name = "ingredient_quantity")
    private Double ingredientQuantity;
}
