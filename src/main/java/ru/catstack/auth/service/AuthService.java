package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.ResourceAlreadyInUseException;
import ru.catstack.auth.exception.TokenRefreshException;
import ru.catstack.auth.exception.UserLogOutException;
import ru.catstack.auth.model.Session;
import ru.catstack.auth.model.User;
import ru.catstack.auth.model.payload.request.LogOutRequest;
import ru.catstack.auth.model.payload.request.LoginRequest;
import ru.catstack.auth.model.payload.request.RegistrationRequest;
import ru.catstack.auth.model.payload.request.TokenRefreshRequest;
import ru.catstack.auth.model.token.RefreshToken;
import ru.catstack.auth.security.jwt.JwtTokenProvider;
import ru.catstack.auth.security.jwt.JwtUser;

import java.util.Optional;

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

    public Boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }

    public Boolean usernameAlreadyExists(String username) {
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
        if (sessionService.isDeviceAlreadyExists(loginRequest.getDeviceInfo())) {
            var session = sessionService.findByDeviceIdAndUserId(loginRequest.getDeviceInfo().getDeviceId(), user.getId());
            session.ifPresent(sess -> refreshTokenService.deleteBySessionId(sess.getId()));
        }
        Session newSession = new Session(user.getId(), loginRequest.getDeviceInfo(), true);
        var refreshToken = refreshTokenService.createRefreshToken(newSession);
        refreshToken = refreshTokenService.save(refreshToken);
        return Optional.ofNullable(refreshToken);
    }

    private void closeAllUserSessionsByUserId(Long id) {
        refreshTokenService.deleteAllByUserId(id);
        sessionService.deleteAllByUserId(id);
    }

    public Optional<String> refreshJwtToken(TokenRefreshRequest tokenRefreshRequest) {
        var token = refreshTokenService.findByToken(tokenRefreshRequest.getRefreshToken());
        if (token.isPresent()) {
            var refreshToken = token.get();
            refreshTokenService.verifyDevice(refreshToken, tokenRefreshRequest);
            refreshTokenService.verifyExpiration(refreshToken);
            sessionService.verifyRefreshAvailability(refreshToken);
            refreshTokenService.increaseCount(refreshToken);
            refreshTokenService.save(refreshToken);

            var session = refreshToken.getUserSession();
            var user = userService.findById(session.getUserId());
            var newToken = generateToken(user.get());
            return Optional.of(newToken);
        }
        throw new TokenRefreshException(tokenRefreshRequest.getRefreshToken(), "Missing refresh token in database. Please login again");
    }

    public void logoutUser(LogOutRequest logOutRequest) {
        var me = userService.getLoggedInUser();
        var deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        var session = sessionService.findByUserId(me.getId())
                .filter(device -> device.getDeviceId().equals(deviceId))
                .orElseThrow(() -> new UserLogOutException(deviceId, "Invalid device Id supplied. No matching session found for the given user"));
        refreshTokenService.deleteBySessionId(session.getId());
        sessionService.deleteByDeviceId(deviceId);
    }

}
