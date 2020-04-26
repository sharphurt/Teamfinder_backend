package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.ResourceAlreadyInUseException;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.model.Member;
import ru.catstack.auth.model.Role;
import ru.catstack.auth.model.Team;
import ru.catstack.auth.model.User;
import ru.catstack.auth.model.payload.request.TeamRegistrationRequest;
import ru.catstack.auth.repository.TeamRepository;
import ru.catstack.auth.util.OffsetBasedPage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final MemberService memberService;

    @Autowired
    TeamService(TeamRepository teamRepository, UserService userService, MemberService memberService) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.memberService = memberService;
    }

    public Optional<Team> registerTeam(TeamRegistrationRequest request) {
        var name = request.getName();
        var me = userService.getLoggedInUser();
        if (nameAlreadyExists(name))
            throw new ResourceAlreadyInUseException("Team name", "value", name);
        var createdTeam = createTeam(me, request);
        return Optional.ofNullable(createdTeam);
    }

    private boolean nameAlreadyExists(String name) {
        return teamRepository.existsByName(name);
    }

    private Team createTeam(User creator, TeamRegistrationRequest request) {
        var creatorMember = memberService.createMember(creator, Set.of(new Role("CREATOR")));
        var team = new Team(request.getName(), request.getDescription(), creatorMember, request.getPicCode());
        team.addMember(creatorMember);
        return save(team);
    }

    public void deleteTeam(long teamId) {
        teamRepository.deleteById(teamId);
    }

    public void addMember(long userId, long teamId) {
        var member = memberService.createMember(getUserOrThrow(userId), Set.of());
        var team = getTeamOrThrow(teamId);
        team.addMember(member);
        save(team);
    }

    public void addMember(Member member, Team team) {
        team.addMember(member);
        save(team);
    }

    public void removeMember(Member member, Team team) {
        team.removeMember(member);
        save(team);
    }

    public void removeMember(long memberId, long teamId) {
        var member = getMemberOrThrow(memberId);
        var team = getTeamOrThrow(teamId);
        team.removeMember(member);
        save(team);
    }

    private User getUserOrThrow(long userId) {
        var optionalUser = userService.findById(userId);
        if (optionalUser.isEmpty())
            throw new ResourceNotFoundException("Member", "member id", userId);
        return optionalUser.get();
    }

    private Member getMemberOrThrow(long memberId) {
        var optionalMember = memberService.findById(memberId);
        if (optionalMember.isEmpty())
            throw new ResourceNotFoundException("Member", "member id", memberId);
        return optionalMember.get();
    }

    private Team getTeamOrThrow(long teamId) {
        var optionalTeam = teamRepository.findById(teamId);
        if (optionalTeam.isEmpty())
            throw new ResourceNotFoundException("Team", "team id", teamId);
        return optionalTeam.get();
    }


    private long teamsCount() {
        return teamRepository.count();
    }

    Optional<Team> getByTeamId(long teamId) {
        return teamRepository.findById(teamId);
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }

    public List<Team> getTeamsGap(int from, int count) {
        return teamRepository.findAll(new OffsetBasedPage(from, count)).getContent();
    }

}
