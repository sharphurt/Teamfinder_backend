package ru.catstack.teamfinder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class UserRegistrationException extends RuntimeException {

    public UserRegistrationException(String user, String message) {
        super(String.format("Failed to register User %s: '%s'", user, message));
    }

}