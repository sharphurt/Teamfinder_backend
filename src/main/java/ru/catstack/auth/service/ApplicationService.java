package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.AccessDeniedException;
import ru.catstack.auth.exception.ApplicationAlreadySentException;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.exception.UserAlreadyInTeamException;
import ru.catstack.auth.model.Application;
import ru.catstack.auth.model.Team;
import ru.catstack.auth.model.User;
import ru.catstack.auth.repository.ApplicationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final TeamService teamService;
    private final UserService userService;
    private final MemberService memberService;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, TeamService teamService, UserService userService, MemberService memberService) {
        this.applicationRepository = applicationRepository;
        this.teamService = teamService;
        this.userService = userService;
        this.memberService = memberService;
    }

    public Optional<Application> createApplication(long teamId) {
        var me = userService.getLoggedInUser();
        return teamService.getByTeamId(teamId).map(team -> {
            var application = new Application(me.getId(), team.getId());
            if (team.getCreator().getUser().getId().equals(me.getId()))
                throw new UserAlreadyInTeamException("You are the creator of the team. You are already in this team");
            if (applicationExists(application))
                throw new ApplicationAlreadySentException(me.getId(), team.getId());
            if (isMemberAlreadyInTeam(team, me))
                throw new UserAlreadyInTeamException("You are already joined that team");
            return Optional.ofNullable(save(application));
        }).orElseThrow(() -> new ResourceNotFoundException("Team", "team id", teamId));
    }

    private boolean isMemberAlreadyInTeam(Team team, User user) {
        var members = team.getMembers();
        for (var member: members) {
            if (member.getUser().getId().equals(user.getId()))
                return true;
        }
        return false;
    }

    public void deleteApplication(long teamId) {
        var me = userService.getLoggedInUser();
        if (applicationExists(new Application(me.getId(), teamId))) {
            applicationRepository.deleteByUserIdAndTeamId(me.getId(), teamId);
        } else
            throw new ResourceNotFoundException("Application", "team id", teamId);
    }

    public List<Application> getApplicationsForTeam(long teamId) {
        var me = userService.getLoggedInUser();
        return teamService.getByTeamId(teamId).map(team -> {
            if (!team.getCreator().getUser().getId().equals(me.getId()))
                throw new AccessDeniedException("You do not have permission to view the list of applications for this team.");
            return applicationRepository.findAllByTeamId(teamId);
        }).orElseThrow(() -> new ResourceNotFoundException("Team", "team id", teamId));
    }

    public List<Application> getApplicationsForUser(long userId) {
        return applicationRepository.findAllByTeamId(userId);
    }

    private Application save(Application application) {
        return applicationRepository.save(application);
    }

    private boolean applicationExists(Application application) {
        return applicationRepository.existsByUserIdAndTeamId(application.getUserId(), application.getTeamId());
    }
}
