package ru.itis.migrants.userservice.exception;

public enum ErrorCode {
    VALIDATION_ERROR("VALIDATION_001"),
    INTERNAL_ERROR("INTERNAL_001"),
    EXTERNAL_SERVICE_ERROR("EXT_001"),

    NOT_FOUND("NOT_FOUND_001"),

    USER_NOT_FOUND("USER_001"),

    USER_ALREADY_EXISTS("USER_002");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
