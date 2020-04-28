package ru.catstack.teamfinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.catstack.teamfinder.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByTeamId(long teamId, Pageable pageable);
}
