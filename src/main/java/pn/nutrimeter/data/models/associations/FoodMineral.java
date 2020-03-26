package pn.nutrimeter.data.models.associations;

import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.Mineral;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "foods_minerals")
@IdClass(FoodMineralId.class)
public class FoodMineral {

    @Id
    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @Id
    @ManyToOne
    @JoinColumn(name = "mineral_id", referencedColumnName = "id")
    private Mineral mineral;

    @Column(name = "mineral_quantity")
    private Double mineralQuantity;
}
