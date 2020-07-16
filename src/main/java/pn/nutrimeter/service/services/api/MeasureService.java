package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.MeasureServiceModel;

import java.util.List;

public interface MeasureService {
    MeasureServiceModel getByName(String name);

    MeasureServiceModel create(MeasureServiceModel model);

    List<MeasureServiceModel> getAllFromList(List<String> measureList);
}
