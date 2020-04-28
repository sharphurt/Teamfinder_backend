package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.ResourceAlreadyInUseException;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.model.*;
import ru.catstack.auth.model.payload.request.TeamRegistrationRequest;
import ru.catstack.auth.repository.TeamRepository;
import ru.catstack.auth.util.OffsetBasedPage;
import ru.catstack.auth.util.Util;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final MemberService memberService;
    private final MessageService messageService;

    @Autowired
    TeamService(TeamRepository teamRepository, UserService userService, MemberService memberService, MessageService messageService) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.memberService = memberService;
        this.messageService = messageService;
    }

    public void registerTeam(TeamRegistrationRequest request) {
        var name = request.getName();
        var me = userService.getLoggedInUser();
        if (nameAlreadyExists(name))
            throw new ResourceAlreadyInUseException("Team name", "value", name);
        createTeam(me, request);
    }

    private boolean nameAlreadyExists(String name) {
        return teamRepository.existsByName(name);
    }

    private void createTeam(User creator, TeamRegistrationRequest request) {
        var creatorMember = memberService.createMember(creator, Set.of(new Role("CREATOR")));
        var team = new Team(request.getName(), request.getDescription(), creatorMember, request.getPicCode());
        team.addMember(creatorMember);
        var saved = save(team);
        messageService.sendCreateMessage(saved, creatorMember);
    }

    public void deleteTeam(long teamId) {
        teamRepository.deleteById(teamId);
    }

    public void addMember(long userId, long teamId) {
        var member = memberService.createMember(userService.getUserOrThrow(userId), Set.of());
        var team = Util.getTeamOrThrow(teamId);
        addMember(member, team);
    }

    void addMember(Member member, Team team) {
        team.addMember(member);
        save(team);
        messageService.sendJoinMessage(team, member);
    }

    private void removeMember(Member member, Team team) {
        team.removeMember(member);
        save(team);
        messageService.sendLeaveMessage(team, member);
    }

    public void removeMember(long memberId, long teamId) {
        var member = getMemberOrThrow(memberId);
        var team = Util.getTeamOrThrow(teamId);
        removeMember(member, team);
    }

    private Member getMemberOrThrow(long memberId) {
        var optionalMember = memberService.findById(memberId);
        if (optionalMember.isEmpty())
            throw new ResourceNotFoundException("Member", "member id", memberId);
        return optionalMember.get();
    }

    private long teamsCount() {
        return teamRepository.count();
    }

    Optional<Team> getByTeamId(long teamId) {
        return teamRepository.findById(teamId);
    }

    private Team save(Team team) {
        return teamRepository.save(team);
    }

    private Sort sort = new Sort(Sort.Direction.DESC, "createdAt");

    public List<Team> getTeamsGap(int from, int count) {
        return teamRepository.findAll(new OffsetBasedPage(from, count, sort)).getContent();
    }

}
