package pn.nutrimeter.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.models.TagServiceModel;
import pn.nutrimeter.service.services.api.FoodCategoryService;
import pn.nutrimeter.service.services.api.TagService;

import java.util.List;

@RestController
public class CategoriesTagsRestController extends BaseRestController {

    private final FoodCategoryService foodCategoryService;

    private final TagService tagService;

    public CategoriesTagsRestController(FoodCategoryService foodCategoryService, TagService tagService) {
        this.foodCategoryService = foodCategoryService;
        this.tagService = tagService;
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @GetMapping("/category/all")
    public ResponseEntity<List<FoodCategoryServiceModel>> allCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(foodCategoryService.getAll());
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @PostMapping("/category/delete/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable String categoryId) {
        this.foodCategoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @GetMapping("/tag/all")
    public ResponseEntity<List<TagServiceModel>> allTags() {
        return ResponseEntity.status(HttpStatus.OK).body(tagService.getAll());
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @PostMapping("/tag/delete/{tagId}")
    public ResponseEntity deleteTag(@PathVariable String tagId) {
        this.tagService.deleteTag(tagId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
