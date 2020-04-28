package ru.catstack.teamfinder.model;

import com.fasterxml.jackson.annotation.*;

public class Sender {
    private Long id;

    private String firstName;

    private String lastName;

    @JsonFormat
    private String avatar;

    public Sender() {
    }

    private Sender(Long id, String firstName, String lastName, String avatar) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    String getAvatar() {
        return avatar;
    }

    static Sender fromUser(User user) {
        return new Sender(user.getId(), user.getFirstName(), user.getLastName(), user.getAvatar());
    }
}
