package ru.catstack.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teams_members",
            joinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")})
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Member> members = Set.of();


    @Column(name = "creator_id")
    private long creatorId;

    public Team(String name, String description, User creator, Set<Member> members, String picCode) {
        this.name = name;
        this.description = description;
        this.members = members;
        this.creatorId = creator.getId();
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

    public Set<Member> getMembers() {
        return members;
    }

    public String getPic() {
        return pic;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
