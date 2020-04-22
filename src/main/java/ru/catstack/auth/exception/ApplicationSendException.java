package ru.catstack.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class ApplicationSendException extends RuntimeException {

    public ApplicationSendException(long teamId) {
        super(String.format("Failed to send application to team %d", teamId));
    }

}