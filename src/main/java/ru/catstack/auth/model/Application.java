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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @JsonIgnore
    @Column(name = "team_id")
    private long teamId;

    public Application(User user, long teamId) {
        this.teamId = teamId;
        this.user = user;
    }

    public Application() {

    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public long getTeamId() {
        return teamId;
    }
}
