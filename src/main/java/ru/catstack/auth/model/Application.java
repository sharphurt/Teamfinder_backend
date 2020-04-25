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
    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "team_id")
    private Team team;

    public Application(User user, Team team) {
        this.team = team;
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

    public Team getTeam() {
        return team;
    }
}
