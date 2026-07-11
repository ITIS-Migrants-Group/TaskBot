package ru.itis.migrants.userservice.advice;

import ru.itis.migrants.userservice.exception.ApiError;
import ru.itis.migrants.userservice.exception.AppException;
import ru.itis.migrants.userservice.exception.ErrorCode;
import ru.itis.migrants.userservice.exception.server.ServerErrorException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiError> handleAppException(AppException ex, WebRequest request) {
        if (ex instanceof ServerErrorException) {
            log.error("Server error occurred: {}", ex.getMessage(), ex);
        } else {
            log.warn("Client error: {} - {}", ex.getHttpStatus().value(), ex.getMessage());
        }
        ApiError apiError = buildApiError(ex, request);
        return new ResponseEntity<>(apiError, ex.getHttpStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Validation failed for request to {}: {}",
                getRequestPath(request), errors);
        ApiError apiError = buildApiError(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                getRequestPath(request),
                ErrorCode.VALIDATION_ERROR.getCode(),
                errors
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        log.error("Unhandled exception occurred at {}: {}",
                getRequestPath(request), ex.getMessage(), ex);
        ApiError apiError = buildApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error. Please try again later.",
                getRequestPath(request),
                ErrorCode.INTERNAL_ERROR.getCode(),
                null
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiError buildApiError(AppException ex, WebRequest request) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getHttpStatus().value())
                .error(ex.getHttpStatus().getReasonPhrase())
                .message(ex.getMessage())
                .path(getRequestPath(request))
                .errorCode(ex.getErrorCode() != null ? ex.getErrorCode().getCode() : null)
                .details(ex.getDetails())
                .build();
    }

    private ApiError buildApiError(HttpStatus status, String message,
                                   String path, String errorCode,
                                   Map<String, String> details) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .errorCode(errorCode)
                .details(details)
                .build();
    }

    private String getRequestPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
            return servletRequest.getRequestURI();
        }
        return "";
    }


}