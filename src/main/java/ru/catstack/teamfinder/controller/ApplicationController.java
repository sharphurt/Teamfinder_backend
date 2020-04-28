package ru.catstack.teamfinder.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.exception.ApplicationSendException;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.ApplicationService;

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

    @GetMapping("/accept")
    public ApiResponse acceptApplication(@RequestParam long applicationId) {
        applicationService.acceptApplication(applicationId);
        return new ApiResponse("Application accepted successfully");
    }

    @GetMapping("/decline")
    public ApiResponse declineApplication(@RequestParam long applicationId) {
        applicationService.declineApplication(applicationId);
        return new ApiResponse("Application declined successfully");
    }
}
