package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.auth.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByPassword(String password);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

}
