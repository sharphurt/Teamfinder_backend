package ru.catstack.auth.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.auth.exception.ApplicationSendException;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.exception.TeamRegistrationException;
import ru.catstack.auth.model.payload.request.TeamRegistrationRequest;
import ru.catstack.auth.model.payload.response.ApiResponse;
import ru.catstack.auth.service.ApplicationService;
import ru.catstack.auth.service.TeamService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/teams/")
public class TeamController {

    private final TeamService teamService;
    private final ApplicationService applicationService;

    @Autowired
    public TeamController(TeamService teamService, ApplicationService applicationService) {
        this.teamService = teamService;
        this.applicationService = applicationService;
    }

    @PostMapping("/register")
    public ApiResponse registerTeam(@Valid @RequestBody TeamRegistrationRequest request) {
        return teamService.registerTeam(request)
                .map(team -> new ApiResponse("Team created successfully"))
                .orElseThrow(() -> new TeamRegistrationException(request.getName(), "Unexpected error"));
    }

    @GetMapping("/get")
    public ApiResponse getTeams(@RequestParam int from, @RequestParam int count) {
        var teams = teamService.getTeamsGap(from, count);
        return new ApiResponse(teams.toArray());
    }

    @GetMapping("/getApplications")
    public ApiResponse getApplications(@RequestParam long teamId) {
        var applications = applicationService.getApplicationsForTeam(teamId);
        return new ApiResponse(applications);
    }

    @GetMapping("/sendApplication")
    public ApiResponse sendApplication(@RequestParam long teamId) {
        return applicationService.createApplication(teamId)
                .map(application -> new ApiResponse("Application sent successfully"))
                .orElseThrow(() -> new ApplicationSendException(teamId));
    }

    @GetMapping("/cancelApplication")
    public ApiResponse cancelApplication(@RequestParam long teamId) {
        applicationService.deleteApplication(teamId);
        return new ApiResponse("Application deleted successfully");
    }
}
