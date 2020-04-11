package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MacroTargetServiceModel {

    private String id;

    private String lifeStageGroupId;

    private Double proteinRDA;

    private Double cysteineMethionineRDA;

    private Double histidineRDA;

    private Double isoleucineRDA;

    private Double leucineRDA;

    private Double lysineRDA;

    private Double phenylalineTyrosineRDA;

    private Double threonineRDA;

    private Double tryptophanRDA;

    private Double valineRDA;

    private Double carbohydrateRDA;

    private Double fiberRDA;

    private Double lipidRDA;

    private Double omega3RDA;

    private Double omega6RDA;

    private Double waterRDA;
}
