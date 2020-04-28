package ru.catstack.teamfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.model.payload.request.setRequest.*;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/users/update/")
public class UpdateUserInfoController {
    private final UserService userService;

    @Autowired
    public UpdateUserInfoController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/username")
    public ApiResponse setUsername(@Valid @RequestBody SetUsername updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateUsernameById(me.getId(), updateInfo.getUsername());
        return new ApiResponse("Username updated successfully");
    }

    @PostMapping("/firstname")
    public ApiResponse setFirstName(@Valid @RequestBody SetFirstName updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateFirstNameById(me.getId(), updateInfo.getFirstName());
        return new ApiResponse("First name updated successfully");
    }

    @PostMapping("/lastname")
    public ApiResponse setLastName(@Valid @RequestBody SetLastName updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateLastNameById(me.getId(), updateInfo.getLastName());
        return new ApiResponse("Last name updated successfully");
    }

    @PostMapping("/age")
    public ApiResponse setAge(@Valid @RequestBody SetAge updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateAgeById(me.getId(), updateInfo.getAge());
        return new ApiResponse("Age updated successfully");
    }

    @PostMapping("/email")
    public ApiResponse setEmail(@Valid @RequestBody SetEmail updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateEmailById(me.getId(), updateInfo.getEmail());
        return new ApiResponse("Email updated successfully");
    }

    @PostMapping("/password")
    public ApiResponse setPassword(@Valid @RequestBody SetPassword updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updatePasswordById(me.getId(), updateInfo.getPassword());
        return new ApiResponse("Password updated successfully");
    }

    @PostMapping("/about")
    public ApiResponse setAboutMe(@Valid @RequestBody SetAboutMe updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateAboutMeById(me.getId(), updateInfo.getAbout());
        return new ApiResponse("Information saved successfully");
    }
}

