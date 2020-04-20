package ru.catstack.auth.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hibernate.annotations.Cascade;
import ru.catstack.auth.model.audit.DateAudit;

import javax.persistence.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "members")
public class Member extends DateAudit {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roles")
    private String roles;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Team> teams;

    public Member(Set<String> roles) {
        setRoles(roles);
    }

    public Member() {

    }

    public Set<String> getRoles() {
        var gson = new Gson();
        var type = new TypeToken<HashSet<String>>(){}.getType();
        return gson.fromJson(this.roles, type);
    }

    public void setRoles(Set<String> roles) {
        var gson = new Gson();
        this.roles = gson.toJson(roles);
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeam(Set<Team> teams) {
        this.teams = teams;
    }

    public Long getId() {
        return id;
    }
}
