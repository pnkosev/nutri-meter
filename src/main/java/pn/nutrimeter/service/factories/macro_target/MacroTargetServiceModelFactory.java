package pn.nutrimeter.service.factories.macro_target;

import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.service.models.MacroTargetServiceModel;

public interface MacroTargetServiceModelFactory {

    MacroTargetServiceModel create(MacroTarget macroTarget, double weight);
}
