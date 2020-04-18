package ru.catstack.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.catstack.auth.model.Status;
import ru.catstack.auth.model.Team;

import java.util.Collection;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Boolean existsByName(String name);

    long count();

    List<Team> findByStatus(Status status, Pageable page);

    Page<Team> findAll(Pageable pageable);
}
