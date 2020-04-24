package ru.catstack.auth.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.auth.exception.ApplicationSendException;
import ru.catstack.auth.exception.TeamRegistrationException;
import ru.catstack.auth.model.payload.request.AddRoleRequest;
import ru.catstack.auth.model.payload.request.TeamRegistrationRequest;
import ru.catstack.auth.model.payload.response.ApiResponse;
import ru.catstack.auth.service.ApplicationService;
import ru.catstack.auth.service.MemberService;
import ru.catstack.auth.service.TeamService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/teams/")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
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
}
