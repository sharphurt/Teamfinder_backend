package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.auth.model.User;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User c SET c.username = :username WHERE c.id = :id")
    void updateUsernameById(@Param("id") Long id, @Param("username") String username);

    @Modifying
    @Query("UPDATE User c SET c.firstName = :firstName WHERE c.id = :id")
    void updateFirstNameById(@Param("id") Long id, @Param("firstName") String firstName);

    @Modifying
    @Query("UPDATE User c SET c.lastName = :lastName WHERE c.id = :id")
    void updateLastNameById(@Param("id") Long id, @Param("lastName") String lastName);

    @Modifying
    @Query("UPDATE User c SET c.age = :age WHERE c.id = :id")
    void updateAgeById(@Param("id") Long id, @Param("age") Byte age);

    @Modifying
    @Query("UPDATE User c SET c.email = :email WHERE c.id = :id")
    void updateEmailById(@Param("id") Long id, @Param("email") String email);

    @Modifying
    @Query("UPDATE User c SET c.password = :password WHERE c.id = :id")
    void updatePasswordById(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Query("UPDATE User c SET c.aboutMe = :aboutMe WHERE c.id = :id")
    void setAboutMeById(@Param("id") Long id, @Param("aboutMe") String aboutMe);

    @Modifying
    @Query("UPDATE User c SET c.updatedAt = :updatedAt WHERE c.id = :id")
    void setUpdatedAtById(@Param("id") Long id, @Param("updatedAt") Instant updatedAt);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.hasPicture = :hasPicture WHERE c.id = :id")
    void updateHasPictureById(@Param("id") Long id, @Param("hasPicture") boolean hasPicture);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
