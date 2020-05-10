package pn.nutrimeter.service.factories.macro_target;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.annotation.Factory;
import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.service.models.MacroTargetServiceModel;

@Factory
public class MacroTargetServiceModelFactoryImpl implements MacroTargetServiceModelFactory {

    private final ModelMapper modelMapper;

    public MacroTargetServiceModelFactoryImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MacroTargetServiceModel create(MacroTarget macroTarget, double weight) {
        MacroTargetServiceModel model = this.modelMapper.map(macroTarget, MacroTargetServiceModel.class);

        model.setCysteineMethionineRDA(macroTarget.getCysteineMethionineRDA() * weight);
        model.setHistidineRDA(macroTarget.getHistidineRDA() * weight);
        model.setIsoleucineRDA(macroTarget.getIsoleucineRDA() * weight);
        model.setLeucineRDA(macroTarget.getLeucineRDA() * weight);
        model.setLysineRDA(macroTarget.getLysineRDA() * weight);
        model.setPhenylalanineTyrosineRDA(macroTarget.getPhenylalanineTyrosineRDA() * weight);
        model.setThreonineRDA(macroTarget.getThreonineRDA() * weight);
        model.setTryptophanRDA(macroTarget.getTryptophanRDA() * weight);
        model.setValineRDA(macroTarget.getValineRDA() * weight);
        model.setOmega3RDA(macroTarget.getOmega3RDA() * weight);
        model.setOmega6RDA(macroTarget.getOmega6RDA() * weight);

        return model;
    }
}
