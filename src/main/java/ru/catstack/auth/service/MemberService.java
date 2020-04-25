package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.AccessDeniedException;
import ru.catstack.auth.exception.ResourceAlreadyInUseException;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.model.Member;
import ru.catstack.auth.model.Role;
import ru.catstack.auth.model.User;
import ru.catstack.auth.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
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

    public Member save(Member member) {
        return memberRepository.save(member);
    }
}
