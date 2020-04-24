package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.model.Member;
import ru.catstack.auth.model.Role;
import ru.catstack.auth.model.User;
import ru.catstack.auth.repository.MemberRepository;

import java.util.Set;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void addRole(long memberId, String role) {
        memberRepository.findById(memberId).ifPresentOrElse(member -> {
            member.addRole(role);
            memberRepository.updateRolesById(memberId, member.getRoles());
        }, () -> {
            throw new ResourceNotFoundException("Member", "member id", memberId);
        });
    }

    Member createMember(User user, Set<Role> roles) {
        var member = new Member(user, roles);
        return memberRepository.save(member);
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }
}
