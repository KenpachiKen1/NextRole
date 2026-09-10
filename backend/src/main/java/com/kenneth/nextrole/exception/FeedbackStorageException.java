package com.kenneth.nextrole.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FeedbackStorageException extends RuntimeException {
    public FeedbackStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
