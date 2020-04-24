package ru.catstack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.catstack.auth.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
