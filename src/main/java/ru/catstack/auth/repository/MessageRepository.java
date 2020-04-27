package ru.catstack.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.auth.model.Message;
import ru.catstack.auth.model.Status;
import ru.catstack.auth.model.Team;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByTeamId(long teamId, Pageable pageable);
}
