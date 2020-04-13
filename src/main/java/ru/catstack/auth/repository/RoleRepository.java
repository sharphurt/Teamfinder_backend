package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.auth.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> { }
