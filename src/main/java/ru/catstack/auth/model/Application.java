package ru.catstack.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @JsonIgnore
    @Column(name = "team_id")
    private long teamId;

    public Application(long userId, long teamId) {
        this.teamId = teamId;
        this.userId = userId;
    }

    public Application() {

    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getTeamId() {
        return teamId;
    }
}
