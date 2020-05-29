package ru.catstack.teamfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.exception.*;
import ru.catstack.teamfinder.model.Application;
import ru.catstack.teamfinder.model.ApplicationStatus;
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
            var app = applicationRepository.findByUserIdAndTeamId(me.getId(), team.getId());
            if (app.isPresent() && app.get().getStatus() == ApplicationStatus.DECLINED) {
                applicationRepository.updateStatusById(app.get().getId(), ApplicationStatus.SENT);
                return app;
            }
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
            var applicationId = applicationIdByUserIdAndTeamId(me.getId(), teamId);
            applicationRepository.deleteById(applicationId);
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
            var applications = applicationRepository.findAllByTeamId(teamId);
            for (var app : applications)
                applicationRepository.updateStatusById(app.getId(), ApplicationStatus.DECLINED);
        }, () -> {
            throw new ResourceNotFoundException("Team", "team id", teamId);
        });
    }

    public void acceptApplication(long applicationId) {
        applicationRepository.findById(applicationId).ifPresentOrElse(application -> {
            if (application.getStatus() == ApplicationStatus.DECLINED)
                throw new InvalidOperationException("You cannot accept a rejected application");
            var me = userService.getLoggedInUser();
            var team = application.getTeam();
            if (!isUserCreatedTeam(me, team))
                throw new AccessDeniedException("You do not have permission to accept applications for this team.");
            var member = memberService.createMember(application.getUser(), Set.of());
            teamService.addMember(member, team);
            applicationRepository.updateStatusById(application.getId(), ApplicationStatus.ACCEPTED);
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
            applicationRepository.updateStatusById(application.getId(), ApplicationStatus.DECLINED);
        }, () -> {
            throw new ResourceNotFoundException("Application", "application id", applicationId);
        });
    }

    public List<Application> getApplicationsForUser(long userId) {
        return applicationRepository.findAllByUserId(userId);
    }

    private Application save(Application application) {
        return applicationRepository.save(application);
    }

    private boolean applicationExistsByUserIdAndTeamId(long userId, long teamId) {
        var app = applicationRepository.findByUserIdAndTeamId(userId, teamId);
        return app.isPresent() && app.get().getStatus() != ApplicationStatus.DECLINED;
    }

    private long applicationIdByUserIdAndTeamId(long userId, long teamId) {
        var app = applicationRepository.findByUserIdAndTeamId(userId, teamId);
        return app.map(Application::getId).orElse(-1L);
    }
}
