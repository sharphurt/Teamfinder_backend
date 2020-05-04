package ru.catstack.teamfinder.model.payload.response;

import ru.catstack.teamfinder.model.Application;
import ru.catstack.teamfinder.model.ApplicationStatus;
import ru.catstack.teamfinder.model.Team;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationResponse {
    private long id;

    private Team team;

    private ApplicationStatus status;

    private ApplicationResponse(long id, Team team, ApplicationStatus status) {
        this.id = id;
        this.team = team;
        this.status = status;
    }

    public ApplicationResponse() {

    }

    public long getId() {
        return id;
    }

    public TeamResponse getTeam() {
        return TeamResponse.fromTeam(team);
    }

    private static ApplicationResponse fromApplication(Application application){
        return new ApplicationResponse(application.getId(), application.getTeam(), application.getStatus());
    }

    public static List<ApplicationResponse> fromApplicationsList(List<Application> applications){
        var result = new ArrayList<ApplicationResponse>();
        for (var app: applications) {
            result.add(ApplicationResponse.fromApplication(app));
        }
        return result;
    }

    public ApplicationStatus getStatus() {
        return status;
    }
}
