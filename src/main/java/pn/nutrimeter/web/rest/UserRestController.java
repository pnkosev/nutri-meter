package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/user/all")
    public List<UserSimpleViewModel> allUsers() {
        return this.userService
                .getAllUsers()
                .stream()
                .map(u -> this.modelMapper.map(u, UserSimpleViewModel.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/user/promote/{userId}")
    public void promoteUser(@PathVariable String userId) {
        this.userService.promoteUser(userId);
    }

    @PostMapping("/user/demote/{userId}")
    public void demoteUser(@PathVariable String userId) {
        this.userService.demoteUser(userId);
    }
}
