package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.MeasureServiceModel;

public interface MeasureService {
    MeasureServiceModel getByName(String name);

    MeasureServiceModel create(MeasureServiceModel model);
}
