package ru.catstack.teamfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.dto.UserDto;
import ru.catstack.teamfinder.exception.NoContentException;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.model.payload.response.ApplicationResponse;
import ru.catstack.teamfinder.service.ApplicationService;
import ru.catstack.teamfinder.service.UserService;

@RestController
@RequestMapping(value = "/api/user/")
public class UserController {
    private final UserService userService;
    private final ApplicationService applicationService;

    @Autowired
    public UserController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping(value = "{id}")
    public ApiResponse getUserById(@PathVariable(name = "id") Long id) {
        var user = userService.findById(id);
        if (user.isEmpty())
            throw new NoContentException("User", "id", id);
        return new ApiResponse(user.get());
    }

    @GetMapping("profile")
    public ApiResponse getUserProfile() {
        var me = userService.getLoggedInUser();
        var userProfileData = userService.findById(me.getId());
        return new ApiResponse(userProfileData);
    }

    @GetMapping("myApplications")
    public ApiResponse getUserApplications() {
        var me = userService.getLoggedInUser();
        var applications = applicationService.getApplicationsForUser(me.getId());
        return new ApiResponse(ApplicationResponse.fromApplicationsList(applications));
    }
}

