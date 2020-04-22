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
        var loggedUser = userService.getLoggedInUser();
        return loggedUser.map(user -> {
            var newToken = userService.updateUsernameById(user.getId(), updateInfo.getUsername());
            return new ApiResponse(newToken);
        }).orElseThrow(() -> new AccessDeniedException("Access denied for unauthenticated user"));
    }

    @PostMapping("/setFirstName")
    public ApiResponse setFirstName(@Valid @RequestBody SetFirstName updateInfo) {
        var loggedUser = userService.getLoggedInUser();
        return loggedUser.map(user -> {
            userService.updateFirstNameById(user.getId(), updateInfo.getFirstName());
            return new ApiResponse("Information saved successfully");
        }).orElseThrow(() -> new AccessDeniedException("Access denied for unauthenticated user"));
    }

    @PostMapping("/setLastName")
    public ApiResponse setLastName(@Valid @RequestBody SetLastName updateInfo) {
        var loggedUser = userService.getLoggedInUser();
        return loggedUser.map(user -> {
            userService.updateLastNameById(user.getId(), updateInfo.getLastName());
            return new ApiResponse("Information saved successfully");
        }).orElseThrow(() -> new AccessDeniedException("Access denied for unauthenticated user"));
    }

    @PostMapping("/setAge")
    public ApiResponse setAge(@Valid @RequestBody SetAge updateInfo) {
        var loggedUser = userService.getLoggedInUser();
        return loggedUser.map(user -> {
            userService.updateAgeById(user.getId(), updateInfo.getAge());
            return new ApiResponse("Information saved successfully");
        }).orElseThrow(() -> new AccessDeniedException("Access denied for unauthenticated user"));
    }

    @PostMapping("/setEmail")
    public ApiResponse setEmail(@Valid @RequestBody SetEmail updateInfo) {
        var loggedUser = userService.getLoggedInUser();
        return loggedUser.map(user -> {
            userService.updateEmailById(user.getId(), updateInfo.getEmail());
            return new ApiResponse("Information saved successfully");
        }).orElseThrow(() -> new AccessDeniedException("Access denied for unauthenticated user"));
    }

    @PostMapping("/setPassword")
    public ApiResponse setPassword(@Valid @RequestBody SetPassword updateInfo) {
        var loggedUser = userService.getLoggedInUser();
        return loggedUser.map(user -> {
            userService.updatePasswordById(user.getId(), updateInfo.getPassword());
            return new ApiResponse("Information saved successfully");
        }).orElseThrow(() -> new AccessDeniedException("Access denied for unauthenticated user"));
    }

    @PostMapping("/setAbout")
    public ApiResponse setAboutMe(@Valid @RequestBody SetAboutMe updateInfo) {
        var loggedUser = userService.getLoggedInUser();
        return loggedUser.map(user -> {
            userService.updateAboutMeById(user.getId(), updateInfo.getAbout());
            return new ApiResponse("Information saved successfully");
        }).orElseThrow(() -> new AccessDeniedException("Access denied for unauthenticated user"));
    }
}

