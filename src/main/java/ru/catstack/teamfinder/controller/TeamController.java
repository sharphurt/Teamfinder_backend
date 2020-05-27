package ru.catstack.teamfinder.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.model.Team;
import ru.catstack.teamfinder.model.payload.request.SearchRequest;
import ru.catstack.teamfinder.model.payload.request.TeamRegistrationRequest;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.TeamService;
import ru.catstack.teamfinder.service.UserService;
import ru.catstack.teamfinder.service.search.TeamsSearchService;
import ru.catstack.teamfinder.util.Util;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/teams/")
public class TeamController {

    private final TeamService teamService;
    private final TeamsSearchService searchService;
    private final UserService userService;

    @Autowired
    public TeamController(TeamService teamService, TeamsSearchService searchService, UserService userService) {
        this.teamService = teamService;
        this.searchService = searchService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse registerTeam(@Valid @RequestBody TeamRegistrationRequest request) {
        teamService.registerTeam(request);
        return new ApiResponse("Team created successfully");
    }

    @GetMapping("/get")
    public ApiResponse getTeams(@RequestParam int from, @RequestParam int count) {
        var teams = teamService.getTeamsGap(from, count);
        return new ApiResponse(Team.toTeamCardList(teams).toArray());
    }

    @DeleteMapping("/delete")
    public ApiResponse deleteTeam(@RequestParam long teamId) {
        teamService.deleteTeam(teamId);
        return new ApiResponse("Team deleted successfully");
    }

    @GetMapping("{id}")
    public ApiResponse getTeamById(@PathVariable(name = "id") long id) {
        return new ApiResponse(Util.fillTeamApplicationStatusField(Util.getTeamOrThrow(id)));
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
        return new ApiResponse(Team.toTeamCardList(Util.fillTeamsApplicationStatusField(
                searchService.findTeams(request.getSearchString(), new String[]{"name", "tagsList"},
                        request.getFrom(), request.getCount()))));
    }

    @GetMapping("/my")
    public ApiResponse getMy(@RequestParam int from, @RequestParam int count) {
        var me = userService.getLoggedInUser();
        return new ApiResponse(Team.toTeamCardList(Util.fillTeamsApplicationStatusField(
                searchService.findTeams(me.getId().toString(), new String[]{"usersList"}, from, count))));
    }
}
