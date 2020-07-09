package pn.nutrimeter.web.rest;

import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.models.TagServiceModel;
import pn.nutrimeter.service.services.api.FoodCategoryService;
import pn.nutrimeter.service.services.api.TagService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoriesTagsRestController {

    private final FoodCategoryService foodCategoryService;

    private final TagService tagService;

    public CategoriesTagsRestController(FoodCategoryService foodCategoryService, TagService tagService) {
        this.foodCategoryService = foodCategoryService;
        this.tagService = tagService;
    }

    @GetMapping("/category/all")
    public List<FoodCategoryServiceModel> allCategories() {
        return foodCategoryService.getAll();
    }

    @PostMapping("/category/delete/{categoryId}")
    public void deleteCategory(@PathVariable String categoryId) {
        this.foodCategoryService.deleteCategory(categoryId);
    }

    @GetMapping("/tag/all")
    public List<TagServiceModel> allTags() {
        return tagService.getAll();
    }

    @PostMapping("/tag/delete/{tagId}")
    public void deleteTag(@PathVariable String tagId) {
        this.tagService.deleteTag(tagId);
    }
}
