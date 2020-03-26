package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.Vitamin;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "foods_vitamins")
@IdClass(FoodVitaminId.class)
public class FoodVitamin {

    @Id
    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @Id
    @ManyToOne
    @JoinColumn(name = "vitamin_id", referencedColumnName = "id")
    private Vitamin vitamin;

    @Column(name = "vitamin_quantity")
    private Double vitaminQuantity;
}
