package ru.itis.migrants.userservice.exception.server;

import org.springframework.http.HttpStatus;
import ru.itis.migrants.userservice.exception.ErrorCode;

import java.util.Map;

public class ExternalServiceException extends ServerErrorException{

    public ExternalServiceException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.EXTERNAL_SERVICE_ERROR);
    }

    public ExternalServiceException(String message, Map<String, String> details) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.EXTERNAL_SERVICE_ERROR, details);
    }

    public ExternalServiceException(String message, HttpStatus status, ErrorCode errorCode) {
        super(message, status, errorCode);
    }

    public ExternalServiceException(String message, HttpStatus status, ErrorCode errorCode, Map<String, String> details) {
        super(message, status, errorCode, details);
    }

    public static ExternalServiceException of(String serviceName, String reason) {
        return new ExternalServiceException(
                String.format("Service '%s' error: %s", serviceName, reason),
                HttpStatus.SERVICE_UNAVAILABLE,
                ErrorCode.EXTERNAL_SERVICE_ERROR,
                Map.of("service", serviceName, "reason", reason)
        );
    }
}