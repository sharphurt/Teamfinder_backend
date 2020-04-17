package ru.catstack.auth.model;

import ru.catstack.auth.model.audit.DateAudit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team extends DateAudit {
    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "pic")
    private String pic;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany
    @JoinTable(name = "teams_members",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "user_id"))
    private Set<User> members;


    public Team(String name, String description, Set<User> members, String picCode) {
        this.name = name;
        this.description = description;
        this.members = members;
        this.pic = picCode;
        this.status = Status.ACTIVE;
    }

    public Team() {

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

    public Set<User> getMembers() {
        return members;
    }

    public String getPic() {
        return pic;
    }

    public Status getStatus() {
        return status;
    }
}
