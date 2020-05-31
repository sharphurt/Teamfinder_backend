package ru.catstack.teamfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.teamfinder.model.Application;
import ru.catstack.teamfinder.model.ApplicationStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByTeamIdAndStatus(long teamId, ApplicationStatus status);

    boolean existsByUserIdAndTeamId(long userId, long teamId);

    @Transactional @Modifying
    @Query("UPDATE Application c SET c.status = :status WHERE c.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") ApplicationStatus status);


    @Transactional
    void deleteByUserIdAndTeamId(long userId, long teamId);

    @Transactional
    void deleteAllByTeamId(long teamId);

    long countAllByTeamId(long teamId);

    List<Application> findAllByUserId(long userId);

    Optional<Application> findByUserIdAndTeamId(long userId, long teamId);
}
