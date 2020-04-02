package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.LifeStageGroupServiceModel;

public interface LifeStageGroupService {

    void create(LifeStageGroupServiceModel lifeStageGroupServiceModel);

    LifeStageGroupServiceModel findById(String id);
}
