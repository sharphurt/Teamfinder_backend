package ru.catstack.teamfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.teamfinder.model.Role;
import ru.catstack.teamfinder.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
