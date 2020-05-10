package ru.catstack.teamfinder.model;

import ru.catstack.teamfinder.model.audit.DateAudit;
import java.util.*;

public class TeamCard {
    private Long id;

    private String name;

    private String description;

    private String pic;

    private int membersCount;

    private List<Long> usersId;

    private Set<Tag> tags;

    private ApplicationStatus applicationStatus = ApplicationStatus.NO_APPLICATION;

    public TeamCard(long id, String name, String description, String picCode, int membersCount, ApplicationStatus applicationStatus, List<Long> usersId, Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.usersId = usersId;
        this.membersCount = membersCount;
        this.applicationStatus = applicationStatus;
        this.tags = tags;
        this.pic = picCode;
    }

    public TeamCard() {

    }

    public static TeamCard fromTeam(Team team) {
        var usersId = new ArrayList<Long>();
        for (var member: team.getMembers())
            usersId.add(member.getUser().getId());

        return new TeamCard(team.getId(),
                team.getName(),
                team.getDescription(),
                team.getPic(),
                team.getMembersCount(),
                team.getApplicationStatus(),
                usersId, team.getTags());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPic() {
        return pic;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public List<Long> getUsersId() {
        return usersId;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }
}
