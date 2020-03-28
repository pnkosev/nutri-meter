package pn.nutrimeter.data.models.micro;

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
@Table(name = "mineral_rdis")
public class MineralRDI extends BaseNutrientRDI {

    @ManyToMany(mappedBy = "mineralRDIs")
    private List<Target> targets;
}
