package pn.nutrimeter.data.models.macro;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.FoodLipid;
import pn.nutrimeter.data.models.base.BaseMacroNutrient;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lipids")
public class Lipid extends BaseMacroNutrient {

    @OneToMany(mappedBy = "lipid")
    private List<FoodLipid> foodLipidAssociation;
}
