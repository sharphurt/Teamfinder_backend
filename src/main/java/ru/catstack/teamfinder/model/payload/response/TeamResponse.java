package ru.catstack.teamfinder.model.payload.response;

import ru.catstack.teamfinder.model.Team;
import java.util.ArrayList;
import java.util.List;

public class TeamResponse {
    private long id;

    private String name;

    private String pic;

    public TeamResponse(long id, String name, String picCode) {
        this.id = id;
        this.name = name;
        this.pic = picCode;
    }

    public TeamResponse() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public static TeamResponse fromTeam(Team team) {
        return new TeamResponse(team.getId(), team.getName(), team.getPic());
    }

    public static List<TeamResponse> fromTeamsList(List<Team> teams) {
        var result = new ArrayList<TeamResponse>();
        for (var team : teams)
            result.add(TeamResponse.fromTeam(team));

        return result;
    }
}
