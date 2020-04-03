package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.service.models.LifeStageGroupServiceModel;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.models.MicroTargetServiceModel;
import pn.nutrimeter.service.services.api.LifeStageGroupService;
import pn.nutrimeter.service.services.api.MacroTargetService;
import pn.nutrimeter.service.services.api.MicroTargetService;
import pn.nutrimeter.web.models.binding.LifeStageGroupBindingModel;
import pn.nutrimeter.web.models.binding.MacroTargetBindingModel;
import pn.nutrimeter.web.models.binding.MicroTargetBindingModel;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/target")
public class TargetController {

    private final LifeStageGroupService lifeStageGroupService;

    private final MacroTargetService macroTargetService;

    private final MicroTargetService microTargetService;

    private final ModelMapper modelMapper;

    public TargetController(LifeStageGroupService lifeStageGroupService, MacroTargetService macroTargetService, MicroTargetService microTargetService, ModelMapper modelMapper) {
        this.lifeStageGroupService = lifeStageGroupService;
        this.macroTargetService = macroTargetService;
        this.microTargetService = microTargetService;
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

    @GetMapping("/macro/add")
    public ModelAndView macroTargetAdd(MacroTargetBindingModel macroTargetBindingModel) {

        List<LifeStageGroupServiceModel> lifeStageGroups = this.lifeStageGroupService.getAll();
        ModelAndView mov = new ModelAndView("target/macro-target-add");
        mov.addObject("lifeStageGroups", lifeStageGroups);

        return mov;
    }

    @PostMapping("/macro/add")
    public String macroTargetAddPost(
            @Valid MacroTargetBindingModel macroTargetBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "target/macro-target-add";
        }

        this.macroTargetService.create(this.modelMapper.map(macroTargetBindingModel, MacroTargetServiceModel.class));

        return "redirect:/";
    }

    @GetMapping("/micro/add")
    public ModelAndView microTargetAdd(MicroTargetBindingModel microTargetBindingModel) {

        List<LifeStageGroupServiceModel> lifeStageGroups = this.lifeStageGroupService.getAll();
        ModelAndView mov = new ModelAndView("target/micro-target-add");
        mov.addObject("lifeStageGroups", lifeStageGroups);

        return mov;
    }

    @PostMapping("/micro/add")
    public String microTargetAddPost(
            @Valid MicroTargetBindingModel microTargetBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "target/macro-target-add";
        }

        this.microTargetService.create(this.modelMapper.map(microTargetBindingModel, MicroTargetServiceModel.class));

        return "redirect:/";
    }
}