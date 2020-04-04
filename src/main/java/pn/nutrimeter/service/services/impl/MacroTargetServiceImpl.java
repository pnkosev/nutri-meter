package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.MacroTargetRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.errors.UserNotFoundException;
import pn.nutrimeter.service.factories.macro_target.MacroTargetServiceModelFactory;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.services.api.MacroTargetService;

@Service
public class MacroTargetServiceImpl implements MacroTargetService {

    private final MacroTargetRepository macroTargetRepository;

    private final MacroTargetServiceModelFactory macroTargetServiceModelFactory;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public MacroTargetServiceImpl(MacroTargetRepository macroTargetRepository, MacroTargetServiceModelFactory macroTargetServiceModelFactory, UserRepository userRepository, ModelMapper modelMapper) {
        this.macroTargetRepository = macroTargetRepository;
        this.macroTargetServiceModelFactory = macroTargetServiceModelFactory;
        this.userRepository = userRepository;
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

    @Override
    public MacroTargetServiceModel getByUserId(String id, double weight) {
        MacroTarget macroTarget = this.macroTargetRepository.findByUserId(id);
        MacroTargetServiceModel model = this.modelMapper.map(macroTarget, MacroTargetServiceModel.class);

        return this.macroTargetServiceModelFactory.create(macroTarget, model, weight);
    }
}
