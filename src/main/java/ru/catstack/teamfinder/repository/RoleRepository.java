package ru.catstack.teamfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.teamfinder.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
