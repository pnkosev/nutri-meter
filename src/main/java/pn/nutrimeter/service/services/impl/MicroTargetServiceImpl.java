package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.MicroTarget;
import pn.nutrimeter.data.repositories.MicroTargetRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.error.UserNotFoundException;
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
    public MicroTargetServiceModel getByLifeStageGroupId(String lifeStageGroupId) {
        MicroTarget microTarget = this.microTargetRepository
                .findByLifeStageGroupId(lifeStageGroupId)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_LIFE_STAGE_GROUP_ID));

        return this.modelMapper.map(microTarget, MicroTargetServiceModel.class);
    }

    @Override
    public MicroTargetServiceModel getByUserId(String id) {
        MicroTarget microTarget = this.microTargetRepository.findByUserId(id);
        return this.modelMapper.map(microTarget, MicroTargetServiceModel.class);
    }
}
