package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.auth.model.Member;
import ru.catstack.auth.model.Role;
import ru.catstack.auth.model.Team;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Transactional @Modifying
    @Query("UPDATE Member m SET m.roles = :roles WHERE m.id = :id")
    void updateRolesById(@Param("id") long id, @Param("roles") Set<Role> roles);
}
