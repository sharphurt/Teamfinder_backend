package ru.catstack.teamfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.catstack.teamfinder.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
