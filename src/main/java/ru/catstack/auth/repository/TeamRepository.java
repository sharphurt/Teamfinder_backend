package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.catstack.auth.model.Team;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Set;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Boolean existsByName(String name);

    long count();
}
