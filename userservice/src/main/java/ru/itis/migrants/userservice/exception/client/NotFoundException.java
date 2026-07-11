package ru.itis.migrants.userservice.exception.client;

import org.springframework.http.HttpStatus;
import ru.itis.migrants.userservice.exception.AppException;
import ru.itis.migrants.userservice.exception.ErrorCode;

import java.util.Map;

public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND);
    }

    public NotFoundException(String message, ErrorCode errorCode) {
        super(message, HttpStatus.NOT_FOUND, errorCode);
    }

    protected NotFoundException(String message, HttpStatus status,
                                ErrorCode errorCode, Map<String, String> details) {
        super(message, status, errorCode, details);
    }

}