package ru.catstack.teamfinder.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String base64Code;

    public Avatar() {
    }

    private Avatar(Long userId, String base64Code) {
        this.userId = userId;
        this.base64Code = base64Code;
    }

    public Long getId() {
        return id;
    }

    public String getBase64Code() {
        return base64Code;
    }

    public Long getUserId() {
        return userId;
    }

    public static Avatar fromUser(User user) {
        return new Avatar(user.getId(), user.getAvatar());
    }
}
