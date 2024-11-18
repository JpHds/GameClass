package com.game_class.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) 
public class DataAlreadyInUseException extends RuntimeException{
    public DataAlreadyInUseException(String message) {
        super(message);
    }
}
