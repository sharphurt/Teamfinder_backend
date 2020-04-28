package ru.catstack.teamfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.exception.AccessDeniedException;
import ru.catstack.teamfinder.exception.ResourceAlreadyInUseException;
import ru.catstack.teamfinder.exception.ResourceNotFoundException;
import ru.catstack.teamfinder.model.Member;
import ru.catstack.teamfinder.model.Role;
import ru.catstack.teamfinder.model.Team;
import ru.catstack.teamfinder.model.User;
import ru.catstack.teamfinder.repository.MemberRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public MemberService(MemberRepository memberRepository, RoleService roleService, UserService userService) {
        this.memberRepository = memberRepository;
        this.roleService = roleService;
        this.userService = userService;
    }

    public void addRole(long memberId, String role) {
        var me = userService.getLoggedInUser();
        memberRepository.findById(memberId).ifPresentOrElse(member -> {
            var team = new ArrayList<>(member.getTeams()).get(0);
            if (!member.getUser().getId().equals(me.getId()) && !team.getCreator().getUser().getId().equals(me.getId()))
                throw new AccessDeniedException("You do not have permission to edit roles list of this user");
            if (isRoleAlreadyInUse(member, role))
                throw new ResourceAlreadyInUseException("Role", "name", role);
            member.addRole(role);
            memberRepository.save(member);
        }, () -> {
            throw new ResourceNotFoundException("Member", "member id", memberId);
        });
    }


    Optional<Member> getMemberByTeam(Team team) {
        var me = userService.getLoggedInUser();
        for (var member : team.getMembers()) {
            if (member.getUser().getId().equals(me.getId()))
                return Optional.of(member);
        }
        return Optional.empty();
    }

    private boolean isRoleAlreadyInUse(Member member, String role) {
        for (var usedRole : member.getRoles()) {
            if (usedRole.getRole().equals(role))
                return true;
        }
        return false;
    }

    public void deleteRole(long roleId) {
        var me = userService.getLoggedInUser();
        roleService.findById(roleId).ifPresentOrElse(role -> {
            var member = new ArrayList<>(role.getMembers()).get(0);
            var team = new ArrayList<>(member.getTeams()).get(0);
            if (!member.getUser().getId().equals(me.getId()) && !team.getCreator().getUser().getId().equals(me.getId()))
                throw new AccessDeniedException("You do not have permission to edit roles list of this user");
            member.getRoles().remove(role);
            memberRepository.save(member);
        }, () -> {
            throw new ResourceNotFoundException("Role", "role id", roleId);
        });
    }

    Member createMember(User user, Set<Role> roles) {
        var member = new Member(user, roles);
        return memberRepository.save(member);
    }

    public Optional<Member> findById(long id){
        return memberRepository.findById(id);
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }
}
