package ru.catstack.teamfinder.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.model.payload.request.SearchRequest;
import ru.catstack.teamfinder.model.payload.request.TeamRegistrationRequest;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.TeamService;
import ru.catstack.teamfinder.service.search.TeamsSearchService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/teams/")
public class TeamController {

    private final TeamService teamService;
    private final TeamsSearchService searchService;

    @Autowired
    public TeamController(TeamService teamService, TeamsSearchService searchService) {
        this.teamService = teamService;
        this.searchService = searchService;
    }

    @PostMapping("/register")
    public ApiResponse registerTeam(@Valid @RequestBody TeamRegistrationRequest request) {
        teamService.registerTeam(request);
        return new ApiResponse("Team created successfully");
    }

    @GetMapping("/get")
    public ApiResponse getTeams(@RequestParam int from, @RequestParam int count) {
        var teams = teamService.getTeamsGap(from, count);
        return new ApiResponse(teams.toArray());
    }

    @GetMapping("/delete")
    public ApiResponse deleteTeam(@RequestParam long teamId) {
        teamService.deleteTeam(teamId);
        return new ApiResponse("Team deleted successfully");
    }

    @GetMapping("/add")
    public ApiResponse addMember(@RequestParam long userId, @RequestParam long teamId) {
        teamService.addMember(userId, teamId);
        return new ApiResponse("Member added successfully");
    }

    @GetMapping("/remove")
    public ApiResponse removeMember(@RequestParam long memberId, @RequestParam long teamId) {
        teamService.removeMember(memberId, teamId);
        return new ApiResponse("Member removed successfully");
    }

    @GetMapping("/search")
    public ApiResponse search(@Valid @RequestBody SearchRequest request) {
        return new ApiResponse(searchService.findTeamsByKeyword(request.getSearchString(), request.getFrom(), request.getCount()));
    }
}
