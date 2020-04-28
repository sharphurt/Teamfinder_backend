package ru.catstack.teamfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.exception.AccessDeniedException;
import ru.catstack.teamfinder.exception.ApplicationAlreadySentException;
import ru.catstack.teamfinder.exception.ResourceNotFoundException;
import ru.catstack.teamfinder.exception.UserAlreadyInTeamException;
import ru.catstack.teamfinder.model.Application;
import ru.catstack.teamfinder.model.Team;
import ru.catstack.teamfinder.model.User;
import ru.catstack.teamfinder.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
            var application = new Application(me, team);
            if (team.getCreator().getUser().getId().equals(me.getId()))
                throw new UserAlreadyInTeamException("You are the creator of the team. You are already in this team");
            if (applicationExistsByUserIdAndTeamId(me.getId(), team.getId()))
                throw new ApplicationAlreadySentException(me.getId(), team.getId());
            if (isMemberAlreadyInTeam(team, me))
                throw new UserAlreadyInTeamException("You are already joined that team");
            return Optional.ofNullable(save(application));
        }).orElseThrow(() -> new ResourceNotFoundException("Team", "team id", teamId));
    }

    private boolean isMemberAlreadyInTeam(Team team, User user) {
        var members = team.getMembers();
        for (var member : members) {
            if (member.getUser().getId().equals(user.getId()))
                return true;
        }
        return false;
    }

    public void deleteApplication(long teamId) {
        var me = userService.getLoggedInUser();
        if (applicationExistsByUserIdAndTeamId(me.getId(), teamId)) {
            applicationRepository.deleteByUserIdAndTeamId(me.getId(), teamId);
        } else
        throw new ResourceNotFoundException("Application", "team id", teamId);
    }

    public List<Application> getApplicationsForTeam(long teamId) {
        var me = userService.getLoggedInUser();
        return teamService.getByTeamId(teamId).map(team -> {
            if (!isUserCreatedTeam(me, team))
                throw new AccessDeniedException("You do not have permission to view the list of applications for this team.");
            return applicationRepository.findAllByTeamId(teamId);
        }).orElseThrow(() -> new ResourceNotFoundException("Team", "team id", teamId));
    }

    private boolean isUserCreatedTeam(User user, Team team) {
        return team.getCreator().getUser().getId().equals(user.getId());
    }

    public void clearApplications(long teamId) {
        var me = userService.getLoggedInUser();
        teamService.getByTeamId(teamId).ifPresentOrElse(team -> {
            if (!isUserCreatedTeam(me, team))
                throw new AccessDeniedException("You do not have permission to clear the list of applications for this team.");
            if (applicationRepository.countAllByTeamId(teamId) == 0)
                throw new ResourceNotFoundException("No team applications found");
            applicationRepository.deleteAllByTeamId(teamId);
        }, () -> {
            throw new ResourceNotFoundException("Team", "team id", teamId);
        });
    }

    public void acceptApplication(long applicationId) {
        applicationRepository.findById(applicationId).ifPresentOrElse(application -> {
            var me = userService.getLoggedInUser();
            var team = application.getTeam();
            if (!isUserCreatedTeam(me, team))
                throw new AccessDeniedException("You do not have permission to accept applications for this team.");
            var member = memberService.createMember(application.getUser(), Set.of());
            teamService.addMember(member, team);
            applicationRepository.deleteByUserIdAndTeamId(member.getUser().getId(), team.getId());
        }, () -> {
            throw new ResourceNotFoundException("Application", "application id", applicationId);
        });
    }

    public void declineApplication(long applicationId) {
        applicationRepository.findById(applicationId).ifPresentOrElse(application -> {
            var me = userService.getLoggedInUser();
            var team = application.getTeam();
            if (!isUserCreatedTeam(me, team))
                throw new AccessDeniedException("You do not have permission to decline applications for this team.");
            applicationRepository.deleteByUserIdAndTeamId(application.getUser().getId(), application.getTeam().getId());
        }, () -> {
            throw new ResourceNotFoundException("Application", "application id", applicationId);
        });
    }

    public List<Application> getApplicationsForUser(long userId) {
        return applicationRepository.findAllByTeamId(userId);
    }

    private Application save(Application application) {
        return applicationRepository.save(application);
    }

    private boolean applicationExistsByUserIdAndTeamId(long userId, long teamId) {
        return applicationRepository.existsByUserIdAndTeamId(userId, teamId);
    }
}
