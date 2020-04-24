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
@RequestMapping(value = "/api/applications/")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/get")
    public ApiResponse getApplications(@RequestParam long teamId) {
        var applications = applicationService.getApplicationsForTeam(teamId);
        return new ApiResponse(applications);
    }

    @GetMapping("/send")
    public ApiResponse sendApplication(@RequestParam long teamId) {
        return applicationService.createApplication(teamId)
                .map(application -> new ApiResponse("Application sent successfully"))
                .orElseThrow(() -> new ApplicationSendException(teamId));
    }

    @GetMapping("/cancel")
    public ApiResponse cancelApplication(@RequestParam long teamId) {
        applicationService.deleteApplication(teamId);
        return new ApiResponse("Application deleted successfully");
    }

    @GetMapping("/clear")
    public ApiResponse clearApplications(@RequestParam long teamId) {
        applicationService.clearApplications(teamId);
        return new ApiResponse("Application deleted successfully");
    }
}
