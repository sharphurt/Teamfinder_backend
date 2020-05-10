package ru.catstack.teamfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.catstack.teamfinder.exception.BadRequestException;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
        if (!name.matches("[a-zA-Zа-яА-Я\\s]{1,30}"))
            throw new BadRequestException("Incorrect tag name");
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