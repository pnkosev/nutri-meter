package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.service.models.DailyStoryServiceModel;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.models.MicroTargetServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;
import pn.nutrimeter.service.services.api.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class DailyStoryController {

    private final UserService userService;

    private final DailyStoryService dailyStoryService;

    private final MacroTargetService macroTargetService;

    private final MicroTargetService microTargetService;

    public DailyStoryController(UserService userService,
                                DailyStoryService dailyStoryService,
                                MacroTargetService macroTargetService,
                                MicroTargetService microTargetService) {
        this.userService = userService;
        this.dailyStoryService = dailyStoryService;
        this.macroTargetService = macroTargetService;
        this.microTargetService = microTargetService;
    }

    @GetMapping("/diary")
    public String dailyStoryDefault() {
        LocalDate today = LocalDate.now();

        return "redirect:/diary/" + today;
    }

    @GetMapping("/diary/{date}")
    public ModelAndView dailyStory(@PathVariable String date, Principal principal) {

        ModelAndView mov = new ModelAndView();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //add try catch
        LocalDate today = LocalDate.parse(date, formatter);

        this.setDays(mov, today);
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        String userId = userServiceModel.getId();
        DailyStoryServiceModel dailyStory = this.dailyStoryService.getByDateAndUserId(today, userId);
        mov.addObject("dailyStory", dailyStory);

        MacroTargetServiceModel macroTargetServiceModel = this.macroTargetService.getByUserId(userId, dailyStory.getDailyWeight());
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
