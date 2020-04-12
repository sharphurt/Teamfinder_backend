package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.auth.model.Session;
import ru.catstack.auth.model.token.RefreshToken;

import java.util.Optional;
import java.util.Set;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Override
    Optional<Session> findById(Long id);

  //  Optional<Session> findByRefreshToken(RefreshToken refreshToken);

    Optional<Session> findByUserId(Long userId);

    void deleteAllByUserId(Long id);

    Byte countAllByUserId(Long id);

    boolean existsByDeviceId(String deviceId);
}
