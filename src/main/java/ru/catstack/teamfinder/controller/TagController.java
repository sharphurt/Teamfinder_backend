package ru.catstack.teamfinder.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.model.payload.request.AddRoleRequest;
import ru.catstack.teamfinder.model.payload.request.AddTagRequest;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.MemberService;
import ru.catstack.teamfinder.service.TeamService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = "/api/teams/tags/")
public class TagController {
    private final TeamService teamService;

    @Autowired
    public TagController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/add")
    public ApiResponse addTag(@RequestParam long teamId, @RequestParam @NotBlank(message = "New tag cannot be blank") String tag) {
        teamService.addTag(teamId, tag);
        return new ApiResponse("Tag added successfully");
    }

    @DeleteMapping("/delete")
    public ApiResponse delete(@RequestParam long tagId) {
        teamService.deleteTag(tagId);
        return new ApiResponse("Tag deleted successfully");
    }
}
