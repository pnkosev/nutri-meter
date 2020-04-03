package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.service.models.DailyStoryServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryService;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.web.models.view.FoodSimpleViewModel;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class DailyStoryController {

    private final FoodService foodService;

    private final DailyStoryService dailyStoryService;

    private final ModelMapper modelMapper;

    public DailyStoryController(FoodService foodService, DailyStoryService dailyStoryService, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.dailyStoryService = dailyStoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/diary")
    public String dailyStoryDefault() {
        LocalDate today = LocalDate.now();

        return "redirect:/diary/" + today;
    }

    @GetMapping("/diary/{date}")
    public ModelAndView dailyStory(@PathVariable String date, HttpSession session) throws ParseException {

        ModelAndView mov = new ModelAndView();
        this.setDays(mov, date);

        List<FoodSimpleViewModel> foods = this.foodService.getAll()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());
        mov.addObject("availableFoods", foods);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.parse(date, formatter);
        String userId = session.getAttribute("userId").toString();

        DailyStoryServiceModel dailyStory = this.dailyStoryService.getByDateAndUserId(currentDate, userId);

        mov.addObject("dailyStoryFoodAss", dailyStory.getDailyStoryFoodAssociation());

        return mov;
    }

    private void setDays(ModelAndView mov, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.parse(date, formatter);
        mov.addObject("today", today);
        LocalDate tomorrow = LocalDate.from(today).plusDays(1);
        LocalDate yesterday = LocalDate.from(today).minusDays(1);
        mov.addObject("yesterday", yesterday);
        mov.addObject("tomorrow", tomorrow);
        mov.setViewName("diary/daily-story");
    }
}
