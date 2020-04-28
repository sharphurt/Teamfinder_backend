package ru.catstack.teamfinder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class TeamRegistrationException extends RuntimeException {

    public TeamRegistrationException(String team, String message) {
        super(String.format("Failed to register Team %s: '%s'", team, message));
    }

}