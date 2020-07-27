package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.data.models.specifications.SearchCriteria;
import pn.nutrimeter.data.models.specifications.UserSpecification;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.web.models.view.UserSimpleViewModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserRestController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public List<UserSimpleViewModel> searchedUsers(@RequestParam(value = "username") String username) {
        UserSpecification specification = new UserSpecification(new SearchCriteria("username", "~", username));
        return this.userService.getAllUsers(specification)
                .stream()
                .map(u -> this.modelMapper.map(u, UserSimpleViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/user/all")
    public List<UserSimpleViewModel> allUsers() {
        return this.userService
                .getAllUsers()
                .stream()
                .map(u -> this.modelMapper.map(u, UserSimpleViewModel.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/user/promote/{userId}")
    public void promoteUser(@PathVariable String userId) { this.userService.promoteUser(userId); }

    @PostMapping("/user/demote/{userId}")
    public void demoteUser(@PathVariable String userId) {
        this.userService.demoteUser(userId);
    }
}
