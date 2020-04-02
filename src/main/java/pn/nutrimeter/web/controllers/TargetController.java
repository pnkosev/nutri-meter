package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pn.nutrimeter.service.models.LifeStageGroupServiceModel;
import pn.nutrimeter.service.services.api.LifeStageGroupService;
import pn.nutrimeter.web.models.binding.LifeStageGroupBindingModel;

import javax.validation.Valid;

@Controller
@RequestMapping("/target")
public class TargetController {

    private final LifeStageGroupService lifeStageGroupService;

    private final ModelMapper modelMapper;

    public TargetController(LifeStageGroupService lifeStageGroupService, ModelMapper modelMapper) {
        this.lifeStageGroupService = lifeStageGroupService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/group/add")
    public String lifeStageGroupAdd(LifeStageGroupBindingModel lifeStageGroupBindingModel) {
        return "target/life-stage-group-add";
    }

    @PostMapping("/group/add")
    public String lifeStageGroupAddPost(
            @Valid LifeStageGroupBindingModel lifeStageGroupBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "target/life-stage-group-add";
        }

        this.lifeStageGroupService.create(this.modelMapper.map(lifeStageGroupBindingModel, LifeStageGroupServiceModel.class));

        return "redirect:/";
    }
}
