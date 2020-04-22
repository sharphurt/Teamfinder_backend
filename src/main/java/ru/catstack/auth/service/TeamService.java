package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.ResourceAlreadyInUseException;
import ru.catstack.auth.model.Member;
import ru.catstack.auth.model.Status;
import ru.catstack.auth.model.Team;
import ru.catstack.auth.model.User;
import ru.catstack.auth.model.payload.request.TeamRegistrationRequest;
import ru.catstack.auth.repository.TeamRepository;
import ru.catstack.auth.util.OffsetBasedPage;

import java.util.Collection;
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
        var newTeam = createTeam(me, request);
        var registeredTeam = save(newTeam);
        return Optional.ofNullable(registeredTeam);
    }

    private boolean nameAlreadyExists(String name) {
        return teamRepository.existsByName(name);
    }

    private Team createTeam(User creator, TeamRegistrationRequest request) {
        return new Team(request.getName(), request.getDescription(), creator, Set.of(new Member(Set.of("Creator"))), request.getPicCode());
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

    public List<Team> getTeamsGap(int from, int count) {
        return teamRepository.findAll(new OffsetBasedPage(from, count)).getContent();
    }

}
