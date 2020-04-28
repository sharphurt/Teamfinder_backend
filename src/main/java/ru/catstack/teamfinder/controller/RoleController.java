package ru.catstack.teamfinder.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.model.payload.request.AddRoleRequest;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.MemberService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/members/roles/")
public class RoleController {
    private final MemberService memberService;

    @Autowired
    public RoleController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/add")
    public ApiResponse addRole(@Valid @RequestBody AddRoleRequest request) {
        memberService.addRole(request.getMemberId(), request.getRole());
        return new ApiResponse("Role added successfully");
    }

    @GetMapping("/delete")
    public ApiResponse delete(long roleId) {
        memberService.deleteRole(roleId);
        return new ApiResponse("Role deleted successfully");
    }
}
