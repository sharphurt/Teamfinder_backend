package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.catstack.auth.model.Role;
import ru.catstack.auth.model.Status;
import ru.catstack.auth.model.User;
import ru.catstack.auth.model.payload.RegistrationRequest;
import ru.catstack.auth.repository.UserProfileDataRepository;
import ru.catstack.auth.repository.UserRepository;
import ru.catstack.auth.security.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserProfileDataRepository userProfileDataRepository;
    private final RefreshTokenService refreshTokenService;
    private final RoleService roleService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, UserProfileDataRepository userProfileDataRepository, RefreshTokenService refreshTokenService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProfileDataRepository = userProfileDataRepository;
        this.refreshTokenService = refreshTokenService;
        this.roleService = roleService;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long Id) {
        return userRepository.findById(Id);
    }

    User save(User user) {
        return userRepository.save(user);
    }

    Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    User createUser(RegistrationRequest registerRequest) {
        return new User(registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getUsername(), registerRequest.getFirstName(), registerRequest.getLastName(),
                registerRequest.getAge(), Set.of(new Role()), Status.ACTIVE
        );
    }

    public Long getUserIdFromRequest(HttpServletRequest request) {
        Optional<String> token = Optional.of(jwtTokenProvider.resolveToken(request));
        return token.map(jwtTokenProvider::getUserId).orElseThrow(() -> new AccessDeniedException("Access denied"));
    }

//    public void logoutUser(@CurrentUser CustomUserDetails currentUser, LogOutRequest logOutRequest) {
//        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
//        UserDevice userDevice = userDeviceService.findByUserId(currentUser.getId())
//                .filter(device -> device.getDeviceId().equals(deviceId))
//                .orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "Invalid device Id supplied. No matching device found for the given user "));
//
//        logger.info("Removing refresh token associated with device [" + userDevice + "]");
//        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
//    }
}
