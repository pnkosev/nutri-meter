package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.LifeStageGroup;
import pn.nutrimeter.data.repositories.LifeStageGroupRepository;
import pn.nutrimeter.service.models.LifeStageGroupServiceModel;
import pn.nutrimeter.service.services.api.LifeStageGroupService;

@Service
public class LifeStageGroupServiceImpl implements LifeStageGroupService {

    private final LifeStageGroupRepository lifeStageGroupRepository;

    private final ModelMapper modelMapper;

    public LifeStageGroupServiceImpl(LifeStageGroupRepository lifeStageGroupRepository, ModelMapper modelMapper) {
        this.lifeStageGroupRepository = lifeStageGroupRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(LifeStageGroupServiceModel lifeStageGroupServiceModel) {
        this.lifeStageGroupRepository.saveAndFlush(this.modelMapper.map(lifeStageGroupServiceModel, LifeStageGroup.class));
    }

    @Override
    public LifeStageGroupServiceModel findById(String id) {
        return this.modelMapper.map(this.lifeStageGroupRepository.findById(id), LifeStageGroupServiceModel.class);
    }
}
