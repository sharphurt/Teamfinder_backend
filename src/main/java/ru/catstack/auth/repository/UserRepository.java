package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
