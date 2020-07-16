package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Measure;
import pn.nutrimeter.data.repositories.MeasureRepository;
import pn.nutrimeter.service.models.MeasureServiceModel;
import pn.nutrimeter.service.services.api.MeasureService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeasureServiceImpl implements MeasureService {

    private final MeasureRepository measureRepository;

    private final ModelMapper modelMapper;

    public MeasureServiceImpl(MeasureRepository measureRepository, ModelMapper modelMapper) {
        this.measureRepository = measureRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MeasureServiceModel getByName(String name) {
        return this.modelMapper.map(this.measureRepository.findByName(name), MeasureServiceModel.class);
    }

    @Override
    public MeasureServiceModel create(MeasureServiceModel model) {
        Measure measure = this.measureRepository.saveAndFlush(this.modelMapper.map(model, Measure.class));
        return this.modelMapper.map(measure, MeasureServiceModel.class);
    }

    @Override
    public List<MeasureServiceModel> getAllFromList(List<String> measureList) {
        return this.measureRepository.findByIdIn(measureList)
                .stream()
                .map(m -> this.modelMapper.map(m, MeasureServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        Measure measure = this.measureRepository.findById(id).get();
        this.measureRepository.delete(measure);
    }
}
