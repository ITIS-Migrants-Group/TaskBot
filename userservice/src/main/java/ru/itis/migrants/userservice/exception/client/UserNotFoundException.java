package ru.itis.migrants.userservice.exception.client;

import ru.itis.migrants.userservice.exception.ErrorCode;

import java.util.Map;

public class UserNotFoundException extends NotFoundException {

    private UserNotFoundException(String message, ErrorCode errorCode, Map<String, String> details) {
        super(message, errorCode);
        if (details != null) {
            setDetails(details);
        }
    }

    private UserNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public static UserNotFoundException byId(Long tgId) {
        return new UserNotFoundException(
                String.format("User with id '%s' not found", tgId),
                ErrorCode.USER_NOT_FOUND
        );
    }

    public static UserNotFoundException byId(Long tgId, Map<String, String> details) {
        return new UserNotFoundException(
                String.format("User with id '%s' not found", tgId),
                ErrorCode.USER_NOT_FOUND,
                details
        );
    }
}