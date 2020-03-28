package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;
import pn.nutrimeter.data.models.macro.CarbohydrateRDI;
import pn.nutrimeter.data.models.macro.LipidRDI;
import pn.nutrimeter.data.models.macro.ProteinRDI;
import pn.nutrimeter.data.models.micro.MineralRDI;
import pn.nutrimeter.data.models.micro.VitaminRDI;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "targets")
public class Target extends BaseEntity {

    @OneToMany(mappedBy = "target")
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "targets_protein_rdis",
            joinColumns = @JoinColumn(name = "target_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "protein_rdi_id", referencedColumnName = "id")
    )
    private List<ProteinRDI> proteinRDIs;

    @ManyToMany
    @JoinTable(
            name = "targets_carbohydrate_rdis",
            joinColumns = @JoinColumn(name = "target_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "carbohydrate_rdi_id", referencedColumnName = "id")
    )
    private List<CarbohydrateRDI> carbohydrateRDIs;

    @ManyToMany
    @JoinTable(
            name = "targets_lipid_rdis",
            joinColumns = @JoinColumn(name = "target_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "lipid_rdi_id", referencedColumnName = "id")
    )
    private List<LipidRDI> lipidRDIs;

    @ManyToMany
    @JoinTable(
            name = "targets_vitamin_rdis",
            joinColumns = @JoinColumn(name = "target_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vitamin_rdi_id", referencedColumnName = "id")
    )
    private List<VitaminRDI> vitaminRDIs;

    @ManyToMany
    @JoinTable(
            name = "targets_mineral_rdis",
            joinColumns = @JoinColumn(name = "target_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "mineral_rdi_id", referencedColumnName = "id")
    )
    private List<MineralRDI> mineralRDIs;
}
