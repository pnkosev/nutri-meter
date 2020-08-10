package pn.nutrimeter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.annotation.PageTitle;
import pn.nutrimeter.error.DateParseFailureException;
import pn.nutrimeter.service.models.*;
import pn.nutrimeter.service.services.api.DailyStoryService;
import pn.nutrimeter.service.services.api.FoodCategoryService;
import pn.nutrimeter.service.services.api.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class DailyStoryController extends BaseController {

    private final UserService userService;

    private final DailyStoryService dailyStoryService;

    private final FoodCategoryService foodCategoryService;

    public DailyStoryController(UserService userService,
                                DailyStoryService dailyStoryService,
                                FoodCategoryService foodCategoryService) {
        this.userService = userService;
        this.dailyStoryService = dailyStoryService;
        this.foodCategoryService = foodCategoryService;
    }

    /**
     * Handling diary get request
     * @param date date
     * @param principal java interface allowing to retrieve current's user username
     * @return ModelAndView
     */
    @GetMapping("/diary/{date}")
    @PageTitle("diary")
    public ModelAndView dailyStory(@PathVariable String date, Principal principal) {

        ModelAndView mav = new ModelAndView();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today;

        try {
            today = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new DateParseFailureException("Please enter a valid date!", e.getParsedString(), e.getErrorIndex());
        }

        this.setDays(mav, today);
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        String userId = userServiceModel.getId();
        DailyStoryServiceModel dailyStory = this.dailyStoryService.getByDateAndUserId(today, userId);
        mav.addObject("dailyStory", dailyStory);

        MacroTargetServiceModel macroTargetServiceModel =
                this.userService.getMacroTargetByUserId(userId, dailyStory.getDailyWeight());
        MicroTargetServiceModel microTargetServiceModel =
                this.userService.getMicroTargetByUserId(userId);
        List<FoodCategoryServiceModel> foodCategoryServiceModels = this.foodCategoryService.getAll();

        mav.addObject("macroTarget", macroTargetServiceModel);
        mav.addObject("microTarget", microTargetServiceModel);
        mav.addObject("categories", foodCategoryServiceModels);
        mav.addObject("totalKcalTarget", userServiceModel.getTotalKcalTarget());
        mav.addObject("kcalFromActivityLevel", userServiceModel.getKcalFromActivityLevel());
        mav.addObject("kcalFromBMR", userServiceModel.getBmr());

        return view(mav, "diary/daily-story");
    }

    private void setDays(ModelAndView mov, LocalDate today) {
        mov.addObject("today", today);
        LocalDate tomorrow = LocalDate.from(today).plusDays(1);
        LocalDate yesterday = LocalDate.from(today).minusDays(1);
        mov.addObject("yesterday", yesterday);
        mov.addObject("tomorrow", tomorrow);
    }
}
