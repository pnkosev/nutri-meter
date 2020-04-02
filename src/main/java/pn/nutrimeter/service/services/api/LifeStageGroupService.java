package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.LifeStageGroupServiceModel;

import java.util.List;

public interface LifeStageGroupService {

    void create(LifeStageGroupServiceModel lifeStageGroupServiceModel);

    LifeStageGroupServiceModel getById(String id);

    List<LifeStageGroupServiceModel> getAll();
}
