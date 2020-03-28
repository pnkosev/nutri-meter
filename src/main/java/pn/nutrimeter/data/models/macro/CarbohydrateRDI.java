package pn.nutrimeter.data.models.macro;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Target;
import pn.nutrimeter.data.models.base.BaseNutrientRDI;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carbohydrate_rdis")
public class CarbohydrateRDI extends BaseNutrientRDI {

    @ManyToMany(mappedBy = "carbohydrateRDIs")
    private List<Target> targets;
}
