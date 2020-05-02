package ru.catstack.teamfinder.controller;

import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.exception.ResourceNotFoundException;
import ru.catstack.teamfinder.model.Avatar;
import ru.catstack.teamfinder.model.payload.request.ImageRequest;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.TeamService;
import ru.catstack.teamfinder.service.UserService;
import ru.catstack.teamfinder.util.Util;

@RestController
@RequestMapping(value = "/api/teams/image")
public class TeamImageController {

    private final TeamService teamService;

    public TeamImageController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("upload")
    public ApiResponse uploadImage(@RequestParam long teamId, @RequestBody ImageRequest imageRequest) {
        teamService.updateAvatarById(teamId, imageRequest.getCode());
        return new ApiResponse("Image was uploaded successfully");
    }

    @GetMapping("get")
    public ApiResponse getImage(@RequestParam long teamId) {
        var team = Util.getTeamOrThrow(teamId);
        return new ApiResponse(team.getPic());
    }

    @GetMapping("delete")
    public ApiResponse deleteImage(@RequestParam long teamId) {
        teamService.updateAvatarById(teamId, null);
        return new ApiResponse("Successfully deleted");
    }
}