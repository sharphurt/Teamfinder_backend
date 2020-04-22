package ru.catstack.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.IM_USED)
public class UserAlreadyInTeamException extends RuntimeException {

    private final Long userId;
    private final Long teamId;

    public UserAlreadyInTeamException(Long userId, Long teamId) {
        super(String.format("User %d is already in team %d", userId, teamId));
        this.userId = userId;
        this.teamId = teamId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTeamId() {
        return teamId;
    }
}