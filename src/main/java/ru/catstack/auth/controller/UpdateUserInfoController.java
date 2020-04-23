package ru.catstack.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.catstack.auth.model.payload.request.setRequest.*;
import ru.catstack.auth.model.payload.response.ApiResponse;
import ru.catstack.auth.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/users/update/")
public class UpdateUserInfoController {
    private final UserService userService;

    @Autowired
    public UpdateUserInfoController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/setUsername")
    public ApiResponse setUsername(@Valid @RequestBody SetUsername updateInfo) {
        var me = userService.getLoggedInUser();
        var newToken = userService.updateUsernameById(me.getId(), updateInfo.getUsername());
        return new ApiResponse(newToken);
    }

    @PostMapping("/setFirstName")
    public ApiResponse setFirstName(@Valid @RequestBody SetFirstName updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateFirstNameById(me.getId(), updateInfo.getFirstName());
        return new ApiResponse("Information saved successfully");
    }

    @PostMapping("/setLastName")
    public ApiResponse setLastName(@Valid @RequestBody SetLastName updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateLastNameById(me.getId(), updateInfo.getLastName());
        return new ApiResponse("Information saved successfully");
    }

    @PostMapping("/setAge")
    public ApiResponse setAge(@Valid @RequestBody SetAge updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateAgeById(me.getId(), updateInfo.getAge());
        return new ApiResponse("Information saved successfully");
    }

    @PostMapping("/setEmail")
    public ApiResponse setEmail(@Valid @RequestBody SetEmail updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateEmailById(me.getId(), updateInfo.getEmail());
        return new ApiResponse("Information saved successfully");
    }

    @PostMapping("/setPassword")
    public ApiResponse setPassword(@Valid @RequestBody SetPassword updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updatePasswordById(me.getId(), updateInfo.getPassword());
        return new ApiResponse("Information saved successfully");
    }

    @PostMapping("/setAbout")
    public ApiResponse setAboutMe(@Valid @RequestBody SetAboutMe updateInfo) {
        var me = userService.getLoggedInUser();
        userService.updateAboutMeById(me.getId(), updateInfo.getAbout());
        return new ApiResponse("Information saved successfully");
    }
}

