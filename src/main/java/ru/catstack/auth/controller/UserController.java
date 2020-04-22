package ru.catstack.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.catstack.auth.dto.UserDto;
import ru.catstack.auth.exception.NoContentException;
import ru.catstack.auth.model.payload.response.ApiResponse;
import ru.catstack.auth.service.UserService;

@RestController
@RequestMapping(value = "/api/users/")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{id}")
    public ApiResponse getUserById(@PathVariable(name = "id") Long id) {
        var user = userService.findById(id);
        if (user.isEmpty())
            throw new NoContentException("User", "id", id);
        UserDto result = UserDto.fromUser(user.get());
        return new ApiResponse(result);
    }

    @GetMapping("profile")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ApiResponse getUserProfile() {
        var loggedUser = userService.getLoggedInUser();
        return loggedUser.map(user -> {
            var userProfileData = userService.findById(user.getId());
            return new ApiResponse(userProfileData);
        }).orElseThrow(() -> new AccessDeniedException("An unexpected error occurred while trying to get user data"));
    }
}

