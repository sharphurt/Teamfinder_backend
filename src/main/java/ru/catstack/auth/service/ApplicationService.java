package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.ApplicationAlreadySentException;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.exception.UserAlreadyInTeamException;
import ru.catstack.auth.model.Application;
import ru.catstack.auth.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final TeamService teamService;
    private final UserService userService;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, TeamService teamService, UserService userService) {
        this.applicationRepository = applicationRepository;
        this.teamService = teamService;
        this.userService = userService;
    }

    public Optional<Application> createApplication(long teamId) {
        var me = userService.getLoggedInUser();
        return teamService.getTeamByTeamId(teamId).map(team -> {
            var application = new Application(me.getId(), team.getId());
            if (applicationExists(application))
                throw new ApplicationAlreadySentException(me.getId(), team.getId());
            if (me.getTeams().contains(team))
                throw new UserAlreadyInTeamException(me.getId(), team.getId());

            return Optional.ofNullable(save(application));
        }).orElseThrow(() -> new ResourceNotFoundException("Team", "team id", teamId));
    }

    public void deleteApplication(long teamId) {
        var me = userService.getLoggedInUser();
        if (applicationExists(new Application(me.getId(), teamId))) {
            applicationRepository.deleteByUserIdAndTeamId(me.getId(), teamId);
        }
        else
            throw new ResourceNotFoundException("Application", "team id", teamId);
    }

    public List<Application> getApplicationsForTeam(Long teamId) {
        return applicationRepository.findAllByTeamId(teamId);
    }

    public List<Application> getApplicationsForUser(Long userId) {
        return applicationRepository.findAllByTeamId(userId);
    }

    private Application save(Application application) {
        return applicationRepository.save(application);
    }

    private boolean applicationExists(Application application) {
        return applicationRepository.existsByUserIdAndTeamId(application.getUserId(), application.getTeamId());
    }
}
