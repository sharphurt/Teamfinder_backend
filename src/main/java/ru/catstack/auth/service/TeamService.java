package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.ResourceAlreadyInUseException;
import ru.catstack.auth.model.Team;
import ru.catstack.auth.model.User;
import ru.catstack.auth.model.payload.request.TeamRegistrationRequest;
import ru.catstack.auth.repository.TeamRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserService userService;

    @Value("{$app.teams.pages.size}")
    private int pageSize;

    @Autowired
    TeamService(TeamRepository teamRepository, UserService userService) {
        this.teamRepository = teamRepository;
        this.userService = userService;
    }

    public Optional<Team> registerTeam(TeamRegistrationRequest request) {
        var name = request.getName();
        var loggedInUser = userService.getLoggedInUser();
        if (nameAlreadyExists(name))
            throw new ResourceAlreadyInUseException("Team name", "value", name);

        return loggedInUser.map(me -> {
            var newTeam = createTeam(me, request);
            var registeredTeam = save(newTeam);
            return Optional.ofNullable(registeredTeam);
        }).orElseThrow(() -> new AccessDeniedException("Unexpected error"));
    }

    private boolean nameAlreadyExists(String name) {
        return teamRepository.existsByName(name);
    }

    private Team createTeam(User me, TeamRegistrationRequest request) {
        return new Team(request.getName(), request.getDescription(), Set.of(me), request.getPicCode());
    }

    private long teamsCount() {
        return teamRepository.count();
    }

    private Team save(Team team) {
        return teamRepository.save(team);
    }

}
