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
@Table(name = "protein_rdis")
public class ProteinRDI extends BaseNutrientRDI {

    @ManyToMany(mappedBy = "proteinRDIs")
    private List<Target> targets;
}
