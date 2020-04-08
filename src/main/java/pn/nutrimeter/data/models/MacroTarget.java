package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;
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

    @Column(name = "cysteine_and_methionine_rda")
    private Double cysteineMethionineRDA;

    @Column(name = "histidine_rda")
    private Double histidineRDA;

    @Column(name = "isoleucine_rda")
    private Double isoleucineRDA;

    @Column(name = "leucine_rda")
    private Double leucineRDA;

    @Column(name = "lysine_rda")
    private Double lysineRDA;

    @Column(name = "phenylaline_and_tyrosine_rda")
    private Double phenylalineTyrosineRDA;

    @Column(name = "threonine_rda")
    private Double threonineRDA;

    @Column(name = "tryptophan_rda")
    private Double tryptophanRDA;

    @Column(name = "valine_rda")
    private Double valineRDA;

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

    @Column(name = "water_rda")
    private Double waterRDA;

    @OneToMany(mappedBy = "macroTarget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;
}
