package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.ResourceAlreadyInUseException;
import ru.catstack.auth.exception.TokenRefreshException;
import ru.catstack.auth.model.Session;
import ru.catstack.auth.model.User;
import ru.catstack.auth.model.payload.request.LoginRequest;
import ru.catstack.auth.model.payload.request.RegistrationRequest;
import ru.catstack.auth.model.payload.request.TokenRefreshRequest;
import ru.catstack.auth.model.token.RefreshToken;
import ru.catstack.auth.security.jwt.JwtTokenProvider;
import ru.catstack.auth.security.jwt.JwtUser;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final SessionService sessionService;

    @Autowired
    public AuthService(UserService userService, JwtTokenProvider tokenProvider, RefreshTokenService refreshTokenService,
                       AuthenticationManager authenticationManager, SessionService sessionService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.sessionService = sessionService;
    }

    public Optional<User> registerUser(RegistrationRequest registrationRequest) {
        var email = registrationRequest.getEmail();
        var username = registrationRequest.getUsername();
        if (emailAlreadyExists(email))
            throw new ResourceAlreadyInUseException("Email", "address", email);
        if (usernameAlreadyExists(username))
            throw new ResourceAlreadyInUseException("Username", "value", username);

        var newUser = userService.createUser(registrationRequest);
        var registeredNewUser = userService.save(newUser);
        return Optional.ofNullable(registeredNewUser);
    }

    private Boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }

    private Boolean usernameAlreadyExists(String username) {
        return userService.existsByUsername(username);
    }

    public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
        return Optional.ofNullable(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())));
    }

    public String generateToken(User user) {
        return tokenProvider.createToken(user);
    }

    public Optional<RefreshToken> createAndPersistRefreshTokenForSession(Authentication auth, LoginRequest loginRequest) {
        var user = (JwtUser) auth.getPrincipal();
        if (sessionService.countAllByUserId(user.getId()) > 4)
            sessionService.deleteAllByUserId(user.getId());
        if (sessionService.isDeviceAlreadyExists(loginRequest.getDeviceInfo()))
            throw new ResourceAlreadyInUseException("Device", "device id", loginRequest.getDeviceInfo().getDeviceId());

        Session newSession = new Session(user.getId(), loginRequest.getDeviceInfo(), true);
        var refreshToken = refreshTokenService.createRefreshToken(newSession);
        refreshToken = refreshTokenService.save(refreshToken);
        return Optional.ofNullable(refreshToken);
    }

    public Optional<String> refreshJwtToken(TokenRefreshRequest tokenRefreshRequest) {
        return Optional.of(refreshTokenService.findByToken(tokenRefreshRequest.getRefreshToken())
                .map(refreshToken -> {
                    refreshTokenService.verifyDevice(refreshToken, tokenRefreshRequest);
                    refreshTokenService.verifyExpiration(refreshToken);
                    sessionService.verifyRefreshAvailability(refreshToken);
                    refreshTokenService.increaseCount(refreshToken);
                    return refreshToken;
                })
                .map(RefreshToken::getUserSession)
                .map(user -> {
                    var userById = userService.findById(user.getId());
                    return userById.get();
                })
                .map(this::generateToken))
                .orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(), "Missing refresh token in database. Please login again"));
    }
}
