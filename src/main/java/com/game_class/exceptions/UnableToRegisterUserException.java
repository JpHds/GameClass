package com.game_class.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableToRegisterUserException extends RuntimeException {
    public UnableToRegisterUserException(String message) {
        super(message);
    }
}
