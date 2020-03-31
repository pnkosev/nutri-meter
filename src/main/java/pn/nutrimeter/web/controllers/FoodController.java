package pn.nutrimeter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/food")
public class FoodController {

    @GetMapping("/add")
    public String add() {
        return "food/food-add";
    }


}
