package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.auth.model.token.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    void deleteByToken(String token);

    @Transactional
    void deleteAllByUserId(Long id);

    @Transactional
    void deleteBySessionId(Long id);
}
