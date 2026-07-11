package ru.itis.migrants.userservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public abstract class AppException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ErrorCode errorCode;
    private Map<String, String> details;

    protected AppException(String message, HttpStatus httpStatus, ErrorCode errorCode) {
        this(message, httpStatus, errorCode, null);
    }

    protected AppException(String message, HttpStatus httpStatus, ErrorCode errorCode,
                           Map<String, String> details) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.details = details;
    }

}