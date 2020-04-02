package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.MicroTarget;
import pn.nutrimeter.data.repositories.MicroTargetRepository;
import pn.nutrimeter.service.models.MicroTargetServiceModel;
import pn.nutrimeter.service.services.api.MicroTargetService;

@Service
public class MicroTargetServiceImpl implements MicroTargetService {

    private final MicroTargetRepository microTargetRepository;

    private final ModelMapper modelMapper;

    public MicroTargetServiceImpl(MicroTargetRepository microTargetRepository, ModelMapper modelMapper) {
        this.microTargetRepository = microTargetRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(MicroTargetServiceModel microTargetServiceModel) {
        this.microTargetRepository.saveAndFlush(this.modelMapper.map(microTargetServiceModel, MicroTarget.class));
    }

    @Override
    public MicroTargetServiceModel getByLifeStageGroupId(MicroTargetServiceModel microTargetServiceModel) {
        MicroTarget microTarget = this.microTargetRepository.findByLifeStageGroupId(microTargetServiceModel.getId());

        return this.modelMapper.map(microTarget, MicroTargetServiceModel.class);
    }
}
