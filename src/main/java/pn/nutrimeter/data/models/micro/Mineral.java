package pn.nutrimeter.data.models.micro;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.FoodMineral;
import pn.nutrimeter.data.models.base.BaseMicroNutrient;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "minerals")
public class Mineral extends BaseMicroNutrient {

    @OneToMany(mappedBy = "mineral")
    private List<FoodMineral> foodMineralAssociation;
}
