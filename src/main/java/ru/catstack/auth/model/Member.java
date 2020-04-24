package ru.catstack.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hibernate.annotations.Cascade;
import ru.catstack.auth.exception.ResourceAlreadyInUseException;
import ru.catstack.auth.model.audit.DateAudit;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "members")
public class Member extends DateAudit {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "members_roles",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @JsonIgnore
    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Team> teams;

    public Member(User user, Set<Role> roles) {
        this.user = user;
        this.roles = roles;
    }

    public Member() {

    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void addRole(String role) {
        this.roles.add(new Role(role));
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
