package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.auth.model.Application;
import ru.catstack.auth.model.User;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByTeamId(long teamId);

    boolean existsByUserIdAndTeamId(long userId, long teamId);

    @Transactional
    void deleteByUserIdAndTeamId(long userId, long teamId);

    @Transactional
    void deleteAllByTeamId(long teamId);

    long countAllByTeamId(long teamId);

    List<Application> findAllByUserId(long userId);
}
