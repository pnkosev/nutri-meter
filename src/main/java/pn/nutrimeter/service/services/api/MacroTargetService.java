package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.MacroTargetServiceModel;

public interface MacroTargetService {

    void create(MacroTargetServiceModel macroTargetServiceModel);

    MacroTargetServiceModel getByLifeGroupId(MacroTargetServiceModel macroTargetServiceModel);
}
