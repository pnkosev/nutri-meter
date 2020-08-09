package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.data.models.specifications.SearchCriteria;
import pn.nutrimeter.data.models.specifications.UserSpecification;
import pn.nutrimeter.error.BaseRuntimeException;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.error.InvalidInputException;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.web.models.view.UserSimpleViewModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserRestController extends BaseRestController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserRestController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserSimpleViewModel>> searchedUsers(@RequestParam(value = "username") String username) {
        // TODO - VALIDATION AND RETURN APPROPRIATE STATUS IF ERROR
        // pseudo code - if (username.isNotValid()) throw new InvalidInputException("message");
        UserSpecification specification =
                new UserSpecification(
                        new SearchCriteria("username", "~", username)
                );
        List<UserSimpleViewModel> users = this.userService.getAllUsers(specification)
                .stream()
                .map(u -> this.modelMapper.map(u, UserSimpleViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserSimpleViewModel>> allUsers() {
        List<UserSimpleViewModel> users = this.userService
                .getAllUsers()
                .stream()
                .map(u -> this.modelMapper.map(u, UserSimpleViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/user/promote/{userId}")
    public ResponseEntity promoteUser(@PathVariable String userId) {
        this.userService.promoteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/user/demote/{userId}")
    public ResponseEntity demoteUser(@PathVariable String userId) {
        this.userService.demoteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
