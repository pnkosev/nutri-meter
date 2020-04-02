package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MacroTargetBindingModel {

    private String lifeStageGroupId;

    private Double proteinRDA;

    private Double carbohydrateRDA;

    private Double fiberRDA;

    private Double lipidRDA;

    private Double omega3RDA;

    private Double omega6RDA;

    private Double saturatedFatRDA;

    private Double transFatRDA;
}
