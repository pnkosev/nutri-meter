package pn.nutrimeter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.error.DateParseFailureException;
import pn.nutrimeter.service.models.DailyStoryServiceModel;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.models.MicroTargetServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryService;
import pn.nutrimeter.service.services.api.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
public class DailyStoryController {

    private final UserService userService;

    private final DailyStoryService dailyStoryService;

    public DailyStoryController(UserService userService, DailyStoryService dailyStoryService) {
        this.userService = userService;
        this.dailyStoryService = dailyStoryService;
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
        LocalDate today;

        try {
            today = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new DateParseFailureException("Please enter a valid date!", e.getParsedString(), e.getErrorIndex());
        }

        this.setDays(mov, today);
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        String userId = userServiceModel.getId();
        DailyStoryServiceModel dailyStory = this.dailyStoryService.getByDateAndUserId(today, userId);
        mov.addObject("dailyStory", dailyStory);

        MacroTargetServiceModel macroTargetServiceModel = this.userService.getMacroTargetByUserId(userId, dailyStory.getDailyWeight());
        MicroTargetServiceModel microTargetServiceModel = this.userService.getMicroTargetByUserId(userId);

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
