package ru.catstack.teamfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.exception.TokenRefreshException;
import ru.catstack.teamfinder.model.Session;
import ru.catstack.teamfinder.model.payload.DeviceInfo;
import ru.catstack.teamfinder.model.token.RefreshToken;
import ru.catstack.teamfinder.repository.RefreshTokenRepository;
import ru.catstack.teamfinder.repository.SessionRepository;

import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final RefreshTokenRepository tokenRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, RefreshTokenRepository tokenRepository) {
        this.sessionRepository = sessionRepository;
        this.tokenRepository = tokenRepository;
    }

    Optional<Session> findByUserId(Long userId) {
        return sessionRepository.findByUserId(userId);
    }

    private Optional<Session> findByRefreshToken(RefreshToken refreshToken) {
        return sessionRepository.findById(tokenRepository.findByToken(refreshToken.getToken()).orElse(new RefreshToken()).getId());
    }

    void verifyRefreshAvailability(RefreshToken refreshToken) {
        Session userSession = findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken.getToken(),
                        "No device found for the matching token. Please login again"));

        if (!userSession.getRefreshActive()) {
            throw new TokenRefreshException(refreshToken.getToken(),
                    "Refresh blocked for the device. Please login through a different device");
        }
    }

    Optional<Session> findByDeviceIdAndUserId(String deviceId, Long userId) {
        return sessionRepository.findByDeviceIdAndUserId(deviceId, userId);
    }

    void deleteById(Long userId) {
        sessionRepository.deleteById(userId);
    }

    Byte countAllByUserId(Long id) {
        return sessionRepository.countAllByUserId(id);
    }

    void deleteAllByUserId(Long id) {
        sessionRepository.deleteAllByUserId(id);
    }

    void deleteByDeviceId(String deviceId) {
        sessionRepository.deleteByDeviceId(deviceId);
    }

    boolean isDeviceAlreadyExists(DeviceInfo deviceInfo) {
        return sessionRepository.existsByDeviceId(deviceInfo.getDeviceId());
    }
}
