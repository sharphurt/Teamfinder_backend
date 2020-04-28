package ru.catstack.teamfinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.teamfinder.model.Status;
import ru.catstack.teamfinder.model.Team;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Boolean existsByName(String name);

    long count();

    @Transactional
    void deleteByIdAndCreator_User_Id(long teamId, long creatorUserId);

    List<Team> findByStatus(Status status, Pageable page);

    Page<Team> findAll(Pageable pageable);
}
