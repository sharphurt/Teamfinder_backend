package ru.catstack.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.IM_USED)
public class UserAlreadyInTeamException extends RuntimeException {
    public UserAlreadyInTeamException(String message) {
        super(message);
    }
}