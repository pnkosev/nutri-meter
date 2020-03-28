package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.macro.Carbohydrate;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "foods_carbohydrates")
@IdClass(FoodCarbohydrateId.class)
public class FoodCarbohydrate {

    @Id
    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @Id
    @ManyToOne
    @JoinColumn(name = "carbohydrate_id", referencedColumnName = "id")
    private Carbohydrate carbohydrate;

    @Column(name = "carbohydrate_quantity")
    private Double carbohydrateQuantity;
}
