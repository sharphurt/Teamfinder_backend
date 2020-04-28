package ru.catstack.teamfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.catstack.teamfinder.model.audit.DateAudit;

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

    @Column(name = "message_type")
    @Enumerated(EnumType.STRING)
    private MessageType type;

    public Message(Team team, Member sender, MessageType type, String message) {
        this.team = team;
        this.sender = sender;
        this.type = type;
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

    public Sender getSender() {
        return Sender.fromUser(sender.getUser());
    }

    public String getMessage() {
        return message;
    }

    public MessageType getType() {
        return type;
    }
}
