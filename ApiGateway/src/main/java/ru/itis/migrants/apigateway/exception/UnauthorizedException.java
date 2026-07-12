package ru.itis.migrants.apigateway.exception;

class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}