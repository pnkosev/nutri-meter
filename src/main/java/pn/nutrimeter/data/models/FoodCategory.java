package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "food_sub_categories")
public class FoodCategory extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "recommended_daily_servings")
    private Integer recommendedDailyServings;

    @Column(name = "amount_in_grams")
    private Double amountInGrams;

    @ManyToMany(mappedBy = "foodCategories")
    private List<Food> foods;

    @ManyToMany(mappedBy = "foodCategories")
    private List<Recipe> recipes;
}
