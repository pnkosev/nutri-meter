package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Measure;
import pn.nutrimeter.data.repositories.MeasureRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.MeasureServiceModel;
import pn.nutrimeter.service.services.api.MeasureService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
    public MeasureServiceModel getById(String id) {
        Measure measure = this.measureRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_MEASURE_ID));
        return this.modelMapper.map(measure, MeasureServiceModel.class);
    }

    @Override
    public List<MeasureServiceModel> createAll(List<MeasureServiceModel> modelList) {
        ArrayList<MeasureServiceModel> list = new ArrayList<>();
        modelList.forEach(m -> {
            Measure measure = this.measureRepository.saveAndFlush(this.modelMapper.map(m, Measure.class));
            MeasureServiceModel measureServiceModel = this.modelMapper.map(measure, MeasureServiceModel.class);
            list.add(measureServiceModel);
        });
        return list;
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
        Measure measure = this.measureRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_MEASURE_ID));
        this.measureRepository.delete(measure);
    }
}
