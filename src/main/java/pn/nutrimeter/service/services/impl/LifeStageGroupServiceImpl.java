package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.LifeStageGroup;
import pn.nutrimeter.data.repositories.LifeStageGroupRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.LifeStageGroupServiceModel;
import pn.nutrimeter.service.services.api.LifeStageGroupService;

import java.util.List;
import java.util.stream.Collectors;

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
    public LifeStageGroupServiceModel getById(String id) {
        LifeStageGroup lifeStageGroup = this.lifeStageGroupRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_LIFE_STAGE_GROUP_ID));

        return this.modelMapper.map(lifeStageGroup, LifeStageGroupServiceModel.class);
    }

    @Override
    public List<LifeStageGroupServiceModel> getAll() {
        return this.lifeStageGroupRepository
                .findAll()
                .stream()
                .map(lsg -> this.modelMapper.map(lsg, LifeStageGroupServiceModel.class))
                .collect(Collectors.toList());
    }
}
