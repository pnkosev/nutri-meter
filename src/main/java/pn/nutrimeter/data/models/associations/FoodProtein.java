package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.macro.Protein;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "foods_proteins")
@IdClass(FoodProteinId.class)
public class FoodProtein {

    @Id
    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @Id
    @ManyToOne
    @JoinColumn(name = "protein_id", referencedColumnName = "id")
    private Protein protein;

    @Column(name = "protein_quantity")
    private Double proteinQuantity;
}
