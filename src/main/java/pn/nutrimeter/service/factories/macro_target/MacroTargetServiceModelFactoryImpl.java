package pn.nutrimeter.service.factories.macro_target;

import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.service.models.MacroTargetServiceModel;

@Service
public class MacroTargetServiceModelFactoryImpl implements MacroTargetServiceModelFactory {

    @Override
    public MacroTargetServiceModel create(MacroTarget macroTarget, MacroTargetServiceModel model, double weight) {

        model.setCysteineMethionineRDA(macroTarget.getCysteineMethionineRDA() * weight);
        model.setHistidineRDA(macroTarget.getHistidineRDA() * weight);
        model.setIsoleucineRDA(macroTarget.getIsoleucineRDA() * weight);
        model.setLeucineRDA(macroTarget.getLeucineRDA() * weight);
        model.setLysineRDA(macroTarget.getLysineRDA() * weight);
        model.setPhenylalineTyrosineRDA(macroTarget.getPhenylalineTyrosineRDA() * weight);
        model.setThreonineRDA(macroTarget.getThreonineRDA() * weight);
        model.setTryptophanRDA(macroTarget.getTryptophanRDA() * weight);
        model.setValineRDA(macroTarget.getValineRDA() * weight);
        model.setValineRDA(macroTarget.getOmega3RDA() * weight);
        model.setValineRDA(macroTarget.getOmega6RDA() * weight);
        model.setSaturatedFatRDA(MacroConstants.SATURATED_FATS_RDA);
        model.setTransFatRDA(MacroConstants.TRANS_FATS_RDA);

        return model;
    }
}
