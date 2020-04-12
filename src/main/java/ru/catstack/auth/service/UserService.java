package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.catstack.auth.model.Role;
import ru.catstack.auth.model.RoleEnum;
import ru.catstack.auth.model.Status;
import ru.catstack.auth.model.User;
import ru.catstack.auth.model.payload.RegistrationRequest;
import ru.catstack.auth.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final RoleService roleService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RefreshTokenService refreshTokenService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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
        User newUser = new User(registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getUsername(), registerRequest.getFirstName(), registerRequest.getLastName(),
                registerRequest.getAge(), Set.of(new Role()), Status.ACTIVE
        );
        return newUser;
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
