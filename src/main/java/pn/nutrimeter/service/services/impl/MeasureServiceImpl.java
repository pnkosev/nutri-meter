package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Measure;
import pn.nutrimeter.data.repositories.MeasureRepository;
import pn.nutrimeter.service.models.MeasureServiceModel;
import pn.nutrimeter.service.services.api.MeasureService;

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
}
