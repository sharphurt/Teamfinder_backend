package ru.catstack.teamfinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Transactional @Modifying
    @Query("UPDATE Team c SET c.pic = :avatar WHERE c.id = :id")
    void updateAvatarById(@Param("id") Long id, @Param("avatar") String avatar);


    List<Team> findByStatus(Status status, Pageable page);

    Page<Team> findAll(Pageable pageable);
}
