package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "macro_targets")
public class MacroTarget extends BaseEntity {

    @Column(name = "life_stage_group_id")
    private String lifeStageGroupId;

    @Column(name = "protein_rda")
    private Double proteinRDA;

    @Column(name = "carbohydrate_rda")
    private Double carbohydrateRDA;

    @Column(name = "fiber_rda")
    private Double fiberRDA;

    @Column(name = "lipid_rda")
    private Double lipidRDA;

    @Column(name = "omega3_rda")
    private Double omega3RDA;

    @Column(name = "omega6_rda")
    private Double omega6RDA;

    @Column(name = "saturated_fat_rda")
    private Double saturatedFatRDA;

    @Column(name = "trans_fat_rda")
    private Double transFatRDA;

    @Column(name = "water_rda")
    private Double waterRDA;

    @OneToMany(mappedBy = "macroTarget")
    private List<User> users;
}
