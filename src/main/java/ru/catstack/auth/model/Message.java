package ru.catstack.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.catstack.auth.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class Message extends DateAudit {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "team_id", referencedColumnName = "team_id")
    private Team team;

    @OneToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "member_id")
    private Member sender;

    @Column(name = "message")
    private String message;

    public Message(Team team, Member sender, String message) {
        this.team = team;
        this.sender = sender;
        this.message = message;
    }

    public Message() {

    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public Member getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
