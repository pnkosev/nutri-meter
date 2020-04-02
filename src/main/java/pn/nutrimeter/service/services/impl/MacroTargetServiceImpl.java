package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.data.repositories.MacroTargetRepository;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.services.api.MacroTargetService;

@Service
public class MacroTargetServiceImpl implements MacroTargetService {

    private final MacroTargetRepository macroTargetRepository;

    private final ModelMapper modelMapper;

    public MacroTargetServiceImpl(MacroTargetRepository macroTargetRepository, ModelMapper modelMapper) {
        this.macroTargetRepository = macroTargetRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(MacroTargetServiceModel macroTargetServiceModel) {
        this.macroTargetRepository.saveAndFlush(this.modelMapper.map(macroTargetServiceModel, MacroTarget.class));
    }

    @Override
    public MacroTargetServiceModel getByLifeGroupId(MacroTargetServiceModel macroTargetServiceModel) {

        MacroTarget macroTarget = this.macroTargetRepository.findByLifeStageGroupId(macroTargetServiceModel.getId());

        return this.modelMapper.map(macroTarget, MacroTargetServiceModel.class);
    }
}
