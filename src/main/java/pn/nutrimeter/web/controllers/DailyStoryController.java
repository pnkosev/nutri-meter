package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.service.models.DailyStoryServiceModel;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.models.MicroTargetServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryService;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.service.services.api.MacroTargetService;
import pn.nutrimeter.service.services.api.MicroTargetService;
import pn.nutrimeter.web.models.view.FoodSimpleViewModel;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DailyStoryController {

    private final FoodService foodService;

    private final DailyStoryService dailyStoryService;

    private final MacroTargetService macroTargetService;

    private final MicroTargetService microTargetService;

    private final ModelMapper modelMapper;

    public DailyStoryController(FoodService foodService, DailyStoryService dailyStoryService, MacroTargetService macroTargetService, MicroTargetService microTargetService, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.dailyStoryService = dailyStoryService;
        this.macroTargetService = macroTargetService;
        this.microTargetService = microTargetService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/diary")
    public String dailyStoryDefault() {
        LocalDate today = LocalDate.now();

        return "redirect:/diary/" + today;
    }

    @GetMapping("/diary/{date}")
    public ModelAndView dailyStory(@PathVariable String date, HttpSession session) {

        ModelAndView mov = new ModelAndView();

        List<FoodSimpleViewModel> foods = this.foodService.getAll()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());
        mov.addObject("availableFoods", foods);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // add a try-catch to the date parse and throw an error if such
        LocalDate today = LocalDate.parse(date, formatter);
        this.setDays(mov, today);
        String userId = session.getAttribute("userId").toString();
        DailyStoryServiceModel dailyStory = this.dailyStoryService.getByDateAndUserId(today, userId);
        mov.addObject("dailyStory", dailyStory);

        MacroTargetServiceModel macroTargetServiceModel = this.macroTargetService.getByUserId(userId);
        MicroTargetServiceModel microTargetServiceModel = this.microTargetService.getByUserId(userId);

        mov.addObject("macroTarget", macroTargetServiceModel);
        mov.addObject("microTarget", microTargetServiceModel);

        return mov;
    }

    private void setDays(ModelAndView mov, LocalDate today) {
        mov.addObject("today", today);
        LocalDate tomorrow = LocalDate.from(today).plusDays(1);
        LocalDate yesterday = LocalDate.from(today).minusDays(1);
        mov.addObject("yesterday", yesterday);
        mov.addObject("tomorrow", tomorrow);
        mov.setViewName("diary/daily-story");
    }
}
