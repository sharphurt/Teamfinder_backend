package ru.catstack.auth.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.catstack.auth.exception.TokenRefreshException;
import ru.catstack.auth.exception.UserLoginException;
import ru.catstack.auth.exception.UserRegistrationException;
import ru.catstack.auth.model.payload.request.LogOutRequest;
import ru.catstack.auth.model.payload.request.LoginRequest;
import ru.catstack.auth.model.payload.request.RegistrationRequest;
import ru.catstack.auth.model.payload.request.TokenRefreshRequest;
import ru.catstack.auth.model.payload.response.ApiResponse;
import ru.catstack.auth.model.payload.response.JwtAuthResponse;
import ru.catstack.auth.model.token.RefreshToken;
import ru.catstack.auth.security.jwt.JwtTokenProvider;
import ru.catstack.auth.security.jwt.JwtUser;
import ru.catstack.auth.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Autowired
    public AuthenticationController(JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @GetMapping("/checkEmailInUse")
    public ApiResponse checkEmailInUse(@RequestParam("email") String email) {
        Boolean emailExists = authService.emailAlreadyExists(email);
        return new ApiResponse(emailExists);
    }

    @GetMapping("/checkUsernameInUse")
    public ApiResponse checkUsernameInUse(@RequestParam("username") String username) {
        Boolean usernameExists = authService.usernameAlreadyExists(username);
        return new ApiResponse(usernameExists);
    }

    @PostMapping("/login")
    public ApiResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication auth = authService
                .authenticateUser(loginRequest)
                .orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));

        var customUserDetails = (JwtUser) auth.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(auth);

        return authService.createAndPersistRefreshTokenForSession(auth, loginRequest)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    var jwtToken = authService.generateToken(customUserDetails.toUser());
                    var response = new JwtAuthResponse(jwtToken, refreshToken, jwtTokenProvider.getValidityDuration());
                    return new ApiResponse(response);
                })
                .orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]"));
    }

    @PostMapping("/register")
    public ApiResponse registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authService.registerUser(registrationRequest)
                .map(user -> new ApiResponse("User registered successfully"))
                .orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(), "Missing user object in database"));
    }

    @PostMapping("/refresh")
    public ApiResponse refreshJwtToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return authService.refreshJwtToken(tokenRefreshRequest)
                .map(updatedToken -> {
                    var refreshToken = tokenRefreshRequest.getRefreshToken();
                    var response = new JwtAuthResponse(updatedToken, refreshToken, jwtTokenProvider.getValidityDuration());
                    return new ApiResponse(response);
                })
                .orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(),
                        "Unexpected error during token refresh. Please logout and login again."));
    }

    @PostMapping("/logout")
    public ApiResponse logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        authService.logoutUser(logOutRequest);
        return new ApiResponse("Log out successfully");
    }
}

