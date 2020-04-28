package ru.catstack.teamfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.teamfinder.model.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Override
    Optional<Session> findById(Long id);

    Optional<Session> findByUserId(Long userId);

    List<Session> findAllByUserId(Long userId);

    Optional<Session> findByDeviceIdAndUserId(String deviceId, Long userId);

    @Transactional
    void deleteAllByUserId(Long id);

    @Transactional
    void deleteByDeviceId(String deviceId);

    Byte countAllByUserId(Long id);

    boolean existsByDeviceId(String deviceId);
}
