package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.MeasureServiceModel;

import java.util.List;

public interface MeasureService {
    MeasureServiceModel getByName(String name);

    MeasureServiceModel getById(String id);

    List<MeasureServiceModel> createAll(List<MeasureServiceModel> model);

    List<MeasureServiceModel> getAllFromList(List<String> measureList);

    void delete(String id);
}
