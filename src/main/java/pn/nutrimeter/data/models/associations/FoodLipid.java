package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.macro.Lipid;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "foods_lipids")
@IdClass(FoodLipidId.class)
public class FoodLipid {

    @Id
    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @Id
    @ManyToOne
    @JoinColumn(name = "lipid_id", referencedColumnName = "id")
    private Lipid lipid;

    @Column(name = "lipid_quantity")
    private Double lipidQuantity;
}
