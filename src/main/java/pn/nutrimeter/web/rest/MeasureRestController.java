package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pn.nutrimeter.service.models.MeasureServiceModel;
import pn.nutrimeter.service.services.api.MeasureService;
import pn.nutrimeter.web.models.binding.MeasureCreateBindingModel;

@RestController
@RequestMapping("/api")
public class MeasureRestController {

    private final MeasureService measureService;

    private final ModelMapper modelMapper;

    public MeasureRestController(MeasureService measureService, ModelMapper modelMapper) {
        this.measureService = measureService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/measure/add")
    public MeasureServiceModel addMeasure(@RequestBody MeasureCreateBindingModel bindingModel) {
        return this.measureService.create(this.modelMapper.map(bindingModel, MeasureServiceModel.class));
    }
}
