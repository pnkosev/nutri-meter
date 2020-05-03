package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.annotation.PageTitle;
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
public class TargetController extends BaseController {

    public static final String LIFE_STAGE_GROUP_ADD_URL = "/group/add";
    public static final String LIFE_STAGE_GROUP_ADD_VIEW = "target/life-stage-group-add";
    public static final String MACRO_TARGET_ADD_URL = "/macro/add";
    public static final String MICRO_TARGET_ADD_URL = "/micro/add";
    public static final String MACRO_TARGET_ADD_VIEW = "target/macro-target-add";
    public static final String MICRO_TARGET_ADD_VIEW = "target/micro-target-add";
    public static final String REDIRECT_URL = "/home";

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

    @GetMapping(LIFE_STAGE_GROUP_ADD_URL)
    @PageTitle("Add Group")
    public ModelAndView lifeStageGroupAdd(LifeStageGroupBindingModel lifeStageGroupBindingModel) {
        return view(LIFE_STAGE_GROUP_ADD_VIEW);
    }

    @PostMapping(LIFE_STAGE_GROUP_ADD_URL)
    public ModelAndView lifeStageGroupAddPost(
            @Valid LifeStageGroupBindingModel lifeStageGroupBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view(LIFE_STAGE_GROUP_ADD_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.lifeStageGroupService.create(this.modelMapper.map(lifeStageGroupBindingModel, LifeStageGroupServiceModel.class));

        return redirect(REDIRECT_URL);
    }

    @GetMapping(MACRO_TARGET_ADD_URL)
    @PageTitle("Add Macro Target")
    public ModelAndView macroTargetAdd(MacroTargetBindingModel macroTargetBindingModel) {

        List<LifeStageGroupServiceModel> lifeStageGroups = this.lifeStageGroupService.getAll();
        ModelAndView mav = new ModelAndView();
        mav.addObject("lifeStageGroups", lifeStageGroups);

        return view(mav, MACRO_TARGET_ADD_VIEW);
    }

    @PostMapping(MACRO_TARGET_ADD_URL)
    public ModelAndView macroTargetAddPost(
            @Valid MacroTargetBindingModel macroTargetBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view(MACRO_TARGET_ADD_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.macroTargetService.create(this.modelMapper.map(macroTargetBindingModel, MacroTargetServiceModel.class));

        return redirect(REDIRECT_URL);
    }

    @GetMapping(MICRO_TARGET_ADD_URL)
    @PageTitle("Add Micro Target")
    public ModelAndView microTargetAdd(MicroTargetBindingModel microTargetBindingModel) {

        List<LifeStageGroupServiceModel> lifeStageGroups = this.lifeStageGroupService.getAll();
        ModelAndView mav = new ModelAndView();
        mav.addObject("lifeStageGroups", lifeStageGroups);

        return view(mav, MICRO_TARGET_ADD_VIEW);
    }

    @PostMapping(MICRO_TARGET_ADD_URL)
    public ModelAndView microTargetAddPost(
            @Valid MicroTargetBindingModel microTargetBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view(MICRO_TARGET_ADD_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.microTargetService.create(this.modelMapper.map(microTargetBindingModel, MicroTargetServiceModel.class));

        return redirect(REDIRECT_URL);
    }
}
