package com.kenneth.nextrole.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class S3StorageException extends RuntimeException {
    public S3StorageException(String message) {
        super(message);
    }
}
