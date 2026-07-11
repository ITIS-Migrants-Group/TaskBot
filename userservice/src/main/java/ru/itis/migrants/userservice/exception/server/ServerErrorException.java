package ru.itis.migrants.userservice.exception.server;


import org.springframework.http.HttpStatus;
import ru.itis.migrants.userservice.exception.AppException;
import ru.itis.migrants.userservice.exception.ErrorCode;

import java.util.Map;

public class ServerErrorException extends AppException {

    public ServerErrorException(String message, HttpStatus status, ErrorCode errorCode) {
        super(message, status, errorCode);
    }

    public ServerErrorException(String message, HttpStatus status, ErrorCode errorCode, Map<String, String> details) {
        super(message, status, errorCode, details);
    }

    public ServerErrorException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR);
    }

    public ServerErrorException(String message, ErrorCode errorCode) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, errorCode);
    }

    public ServerErrorException(String message, ErrorCode errorCode, Map<String, String> details) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, errorCode, details);
    }
}