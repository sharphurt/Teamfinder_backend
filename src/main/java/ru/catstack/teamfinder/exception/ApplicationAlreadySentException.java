package ru.catstack.teamfinder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.IM_USED)
public class ApplicationAlreadySentException extends RuntimeException {

    private final Long userId;
    private final Long teamId;

    public ApplicationAlreadySentException(Long userId, Long teamId) {
        super(String.format("Application was already sent by user %d to team %d", userId, teamId));
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