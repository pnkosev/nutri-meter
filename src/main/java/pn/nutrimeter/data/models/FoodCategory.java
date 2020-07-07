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
@Table(name = "food_categories")
public class FoodCategory extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "foodCategories")
    private List<Food> foods;
}
