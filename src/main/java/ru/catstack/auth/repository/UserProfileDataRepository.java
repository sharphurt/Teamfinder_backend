package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.auth.model.UserProfileData;

import java.util.Optional;

public interface UserProfileDataRepository extends JpaRepository<UserProfileData, Long> {
    Optional<UserProfileData> findByUserId(Long id);
}
