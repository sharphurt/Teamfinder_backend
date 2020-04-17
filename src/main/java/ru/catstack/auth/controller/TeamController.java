package ru.catstack.auth.controller;


import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.auth.exception.TeamRegistrationException;
import ru.catstack.auth.model.payload.request.TeamRegistrationRequest;
import ru.catstack.auth.model.payload.response.ApiResponse;
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

    @GetMapping("/page/{page}")
    public ApiResponse registerTeam(@PathVariable(value = "page") Long page) {
        throw new NotYetImplementedException();
    }
}
