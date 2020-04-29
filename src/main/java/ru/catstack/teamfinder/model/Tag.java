package ru.catstack.teamfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag implements Serializable {

    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String tag;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Team> teams;

    public Tag(String name) {
        this.tag = name;
    }

    public Tag() {    }

    public Long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public Set<Team> getTeams() {
        return teams;
    }
}