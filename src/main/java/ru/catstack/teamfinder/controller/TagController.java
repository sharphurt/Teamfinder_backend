package ru.catstack.teamfinder.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.model.payload.request.AddRoleRequest;
import ru.catstack.teamfinder.model.payload.request.AddTagRequest;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.MemberService;
import ru.catstack.teamfinder.service.TeamService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/teams/tags/")
public class TagController {
    private final TeamService teamService;

    @Autowired
    public TagController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/add")
    public ApiResponse addTag(@Valid @RequestBody AddTagRequest request) {
        teamService.addTag(request.getTeamId(), request.getTag());
        return new ApiResponse("Tag added successfully");
    }

    @GetMapping("/delete")
    public ApiResponse delete(long tagId) {
        teamService.deleteTag(tagId);
        return new ApiResponse("Tag deleted successfully");
    }
}
