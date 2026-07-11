package ru.itis.migrants.userservice.exception.client;

import org.springframework.http.HttpStatus;
import ru.itis.migrants.userservice.exception.AppException;
import ru.itis.migrants.userservice.exception.ErrorCode;

import java.util.Map;

public class UserAlreadyExistsException extends AppException {
    protected UserAlreadyExistsException(String message, HttpStatus httpStatus, ErrorCode errorCode) {
        super(message, httpStatus, errorCode);
    }

    protected UserAlreadyExistsException(String message, HttpStatus httpStatus, ErrorCode errorCode, Map<String, String> details) {
        super(message, httpStatus, errorCode, details);
    }

    protected UserAlreadyExistsException(String message, ErrorCode errorCode) {
        super(message, HttpStatus.CONFLICT, errorCode);
    }


    public static UserAlreadyExistsException byId(Long tgId) {
        return new UserAlreadyExistsException(
                String.format("User with id '%s' already exists", tgId),
                ErrorCode.USER_ALREADY_EXISTS
        );
    }
}
