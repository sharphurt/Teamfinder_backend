package ru.catstack.teamfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.teamfinder.model.User;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);


    @Transactional @Modifying
    @Query("UPDATE User c SET c.username = :username WHERE c.id = :id")
    void updateUsernameById(@Param("id") Long id, @Param("username") String username);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.firstName = :firstName WHERE c.id = :id")
    void updateFirstNameById(@Param("id") Long id, @Param("firstName") String firstName);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.lastName = :lastName WHERE c.id = :id")
    void updateLastNameById(@Param("id") Long id, @Param("lastName") String lastName);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.age = :age WHERE c.id = :id")
    void updateAgeById(@Param("id") Long id, @Param("age") Byte age);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.email = :email WHERE c.id = :id")
    void updateEmailById(@Param("id") Long id, @Param("email") String email);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.password = :password WHERE c.id = :id")
    void updatePasswordById(@Param("id") Long id, @Param("password") String password);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.aboutMe = :aboutMe WHERE c.id = :id")
    void updateAboutMeById(@Param("id") Long id, @Param("aboutMe") String aboutMe);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.updatedAt = :updatedAt WHERE c.id = :id")
    void setUpdatedAtById(@Param("id") Long id, @Param("updatedAt") Instant updatedAt);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.avatar = :avatar WHERE c.id = :id")
    void updateAvatarById(@Param("id") Long id, @Param("avatar") String avatar);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
