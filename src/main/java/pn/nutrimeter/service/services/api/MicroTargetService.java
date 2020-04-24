package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.MicroTargetServiceModel;

public interface MicroTargetService {

    void create(MicroTargetServiceModel microTargetServiceModel);

    MicroTargetServiceModel getByLifeStageGroupId(String lifeStageGroupId);

    MicroTargetServiceModel getByUserId(String id);
}
