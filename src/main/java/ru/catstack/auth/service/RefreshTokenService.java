package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.TokenRefreshException;
import ru.catstack.auth.model.Session;
import ru.catstack.auth.model.payload.TokenRefreshRequest;
import ru.catstack.auth.model.token.RefreshToken;
import ru.catstack.auth.repository.RefreshTokenRepository;

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
        return new RefreshToken(UUID.randomUUID().toString(), session, 0L,
                Instant.now().plusMillis(refreshTokenDurationMs));
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

    void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    void increaseCount(RefreshToken refreshToken) {
        refreshToken.incrementRefreshCount();
        save(refreshToken);
    }
}
