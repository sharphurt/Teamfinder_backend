package ru.catstack.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.catstack.auth.dto.UserDto;
import ru.catstack.auth.model.User;
import ru.catstack.auth.model.UserProfileData;
import ru.catstack.auth.model.payload.ApiResponse;
import ru.catstack.auth.model.payload.SetUserInfoRequest;
import ru.catstack.auth.security.jwt.JwtUser;
import ru.catstack.auth.service.UserProfileDataService;
import ru.catstack.auth.service.UserService;

import javax.validation.Valid;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/users/")
public class UserController {
    private final UserService userService;
    private final UserProfileDataService userProfileDataService;
    private final Logger logger = Logger.getLogger(UserController.class.getName());


    @Autowired
    public UserController(UserService userService, UserProfileDataService userProfileDataService) {
        this.userService = userService;
        this.userProfileDataService = userProfileDataService;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        Optional user = userService.findById(id);
        if (user.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        UserDto result = UserDto.fromUser((User) user.get());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/aboutMe")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity getAboutMe() {
        Optional<JwtUser> loggedUser = Optional.of(getLoggedInUser());
        return loggedUser.map(user -> {
            logger.info(user.getEmail() + " has roles: " + user.getAuthorities());
            Long userId = user.getId();
            Optional<UserProfileData> userProfileData = userProfileDataService.findByUserId(userId);
            return userProfileData.map(
                    userData -> ResponseEntity.ok(new ApiResponse(true, userData.getAboutMe())))
                    .orElse(ResponseEntity.ok(new ApiResponse(false, "User has no personal information")));
        }).orElseThrow(() -> new AccessDeniedException("Access denied for unauthenticated user"));
    }

    @PostMapping("/setAboutMe")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity setAboutMe(@Valid @RequestBody SetUserInfoRequest userInfoRequest) {
        Optional<JwtUser> loggedUser = Optional.of(getLoggedInUser());
        return loggedUser.map(user -> {
            UserProfileData userProfileData = new UserProfileData(user.getId(), userInfoRequest.getAboutMe());
            UserProfileData savedData = userProfileDataService.save(userProfileData);
            logger.info("User profile data saved for user with id " + savedData.getUserId() + ": " + savedData.getAboutMe());
            return ResponseEntity.ok(new ApiResponse(true, "Information saved successfully"));
        }).orElseThrow(() -> new AccessDeniedException("Access denied for unauthenticated user"));
    }

    private JwtUser getLoggedInUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken))
            return (JwtUser) auth.getPrincipal();
        return null;
    }
}
