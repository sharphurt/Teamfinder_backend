package ru.catstack.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.catstack.auth.exception.TokenRefreshException;
import ru.catstack.auth.exception.UserLoginException;
import ru.catstack.auth.exception.UserRegistrationException;
import ru.catstack.auth.model.payload.*;
import ru.catstack.auth.model.token.RefreshToken;
import ru.catstack.auth.security.jwt.JwtTokenProvider;
import ru.catstack.auth.security.jwt.JwtUser;
import ru.catstack.auth.service.AuthService;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {

    private final Logger logger = Logger.getLogger(AuthenticationController.class.getName());
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Autowired
    public AuthenticationController(JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication auth = authService.authenticateUser(loginRequest)
                .orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));

        JwtUser customUserDetails = (JwtUser) auth.getPrincipal();
        logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return authService.createAndPersistRefreshTokenForSession(auth, loginRequest)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    String jwtToken = authService.generateToken(customUserDetails.toUser());
                    return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken, jwtTokenProvider.getValidityDuration()));
                })
                .orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]"));
    }

    @PostMapping("register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authService.registerUser(registrationRequest)
                .map(user -> ResponseEntity.ok(new ApiResponse(true, "User registered successfully")))
                .orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(), "Missing user object in database"));
    }

    @PostMapping("refresh")
    public ResponseEntity refreshJwtToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return authService.refreshJwtToken(tokenRefreshRequest)
                .map(updatedToken -> {
                    String refreshToken = tokenRefreshRequest.getRefreshToken();
                    logger.info("Created new Jwt Auth token: " + updatedToken);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(updatedToken, refreshToken, jwtTokenProvider.getValidityDuration()));
                })
                .orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(),
                        "Unexpected error during token refresh. Please logout and login again."));
    }
}

