package pn.nutrimeter.data.models.macro;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.FoodProtein;
import pn.nutrimeter.data.models.base.BaseMacroNutrient;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "proteins")
public class Protein extends BaseMacroNutrient {

    @OneToMany(mappedBy = "protein")
    private List<FoodProtein> foodProteinAssociation;
}
