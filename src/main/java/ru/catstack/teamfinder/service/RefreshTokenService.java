package ru.catstack.teamfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.exception.TokenRefreshException;
import ru.catstack.teamfinder.model.Session;
import ru.catstack.teamfinder.model.payload.request.TokenRefreshRequest;
import ru.catstack.teamfinder.model.token.RefreshToken;
import ru.catstack.teamfinder.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.token.refresh.duration}")
    private Long refreshTokenDurationMs;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    RefreshToken createRefreshToken(Session session) {
        return new RefreshToken(UUID.randomUUID().toString(), session, session.getUserId(), 0L, Instant.now().plusMillis(refreshTokenDurationMs));
    }

    void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new TokenRefreshException(token.getToken(), "Expired token. Please issue a new request");
        }
    }

    void verifyDevice(RefreshToken token, TokenRefreshRequest refreshRequest) {
        if (!token.getUserSession().getDeviceId().equals(refreshRequest.getDeviceInfo().getDeviceId())) {
            deleteByToken(token.getToken());
            throw new TokenRefreshException(token.getToken(), "Expired token. Please issue a new request");
        }
    }

    private void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    void deleteBySessionId(Long id) {
        refreshTokenRepository.deleteBySessionId(id);
    }

    void deleteAllByUserId(Long id) {
        refreshTokenRepository.deleteAllByUserId(id);
    }

    void increaseCount(RefreshToken refreshToken) {
        refreshToken.incrementRefreshCount();
    }
}
