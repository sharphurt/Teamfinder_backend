package ru.catstack.teamfinder.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.exception.ResourceNotFoundException;
import ru.catstack.teamfinder.model.Application;
import ru.catstack.teamfinder.model.ApplicationStatus;
import ru.catstack.teamfinder.model.Team;
import ru.catstack.teamfinder.repository.ApplicationRepository;
import ru.catstack.teamfinder.repository.TeamRepository;
import ru.catstack.teamfinder.service.ApplicationService;
import ru.catstack.teamfinder.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class Util {
    private static TeamRepository teamRepository;
    private static ApplicationRepository applicationRepository;
    private static UserService userService;

    @Autowired
    public Util(TeamRepository teamRepository, ApplicationRepository applicationRepository, UserService userService) {
        Util.teamRepository = teamRepository;
        Util.applicationRepository = applicationRepository;
        Util.userService = userService;
    }

    public static boolean IsTeamContainsUser(Team team, long userId) {
        if (team.getCreator().getUser().getId().equals(userId))
            return true;
        for (var member : team.getMembers()) {
            if (member.getUser().getId().equals(userId))
                return true;
        }
        return false;
    }

    public static Team getTeamOrThrow(long teamId) {
        var optionalTeam = teamRepository.findById(teamId);
        if (optionalTeam.isEmpty())
            throw new ResourceNotFoundException("Team", "team id", teamId);
        return optionalTeam.get();
    }

    public static Optional<Application> findByUserIdAndTeamId(long userId, long teamId) {
        return applicationRepository.findByUserIdAndTeamId(userId, teamId);
    }

    public static List<Team> fillApplicationStatusField(List<Team> teams) {
        var me = userService.getLoggedInUser();
        for (var team : teams) {
            if (team.getCreator().getUser().getId().equals(me.getId()))
                team.setApplicationStatus(ApplicationStatus.TEAM_CREATOR);
            else if (Util.findByUserIdAndTeamId(me.getId(), team.getId()).isPresent())
                team.setApplicationStatus(Util.findByUserIdAndTeamId(me.getId(), team.getId()).get().getStatus());
            else
                team.setApplicationStatus(ApplicationStatus.NO_APPLICATION);
        }
        return teams;
    }

}
