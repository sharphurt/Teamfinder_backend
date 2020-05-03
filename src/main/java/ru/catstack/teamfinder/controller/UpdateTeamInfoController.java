package ru.catstack.teamfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.model.payload.request.setRequest.*;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.TeamService;
import ru.catstack.teamfinder.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/teams/update/")
public class UpdateTeamInfoController {
    private final TeamService teamService;

    @Autowired
    public UpdateTeamInfoController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/name")
    public ApiResponse setUsername(@Valid @RequestBody SetTeamName updateInfo, @RequestParam long teamId) {
        teamService.updateNameById(teamId, updateInfo.getName());
        return new ApiResponse("Team name updated successfully");
    }
}

