package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.catstack.auth.model.Role;
import ru.catstack.auth.model.Status;
import ru.catstack.auth.model.User;
import ru.catstack.auth.model.UserProfileData;
import ru.catstack.auth.model.payload.RegistrationRequest;
import ru.catstack.auth.repository.UserProfileDataRepository;
import ru.catstack.auth.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class UserProfileDataService {

    private static final Logger logger = Logger.getLogger(UserProfileDataService.class.getName());
    private final UserRepository userRepository;
    private final UserProfileDataRepository userProfileDataRepository;

    @Autowired
    public UserProfileDataService(UserRepository userRepository, UserProfileDataRepository userProfileDataRepository) {
        this.userRepository = userRepository;
        this.userProfileDataRepository = userProfileDataRepository;
    }

    public UserProfileData save(UserProfileData userProfileData) {
        return userProfileDataRepository.save(userProfileData);
    }

    public Optional<UserProfileData> findByUserId(Long userId) {
        return userProfileDataRepository.findByUserId(userId);
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
