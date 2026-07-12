package ru.itis.migrants.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.itis.migrants.apigateway.dto.response.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponse> handleWebClientException(
            WebClientResponseException ex
    ) {
        var errorResponse = new ErrorResponse(
                ex.getStatusCode().value(),
                "Ошибка в микросервисе: " + ex.getStatusCode(),
                ex.getResponseBodyAsString(),
                LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        
        var errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации",
                String.join(", ", errors),
                LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex
    ) {
        var errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Ресурс не найден",
                ex.getMessage(),
                LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex
    ) {
        var errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Не авторизован",
                ex.getMessage(),
                LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex
    ) {
        var errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Внутренняя ошибка сервера",
                "Попробуйте позже",
                LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(java.util.concurrent.TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(
            java.util.concurrent.TimeoutException ex
    ) {
        var errorResponse = new ErrorResponse(
                HttpStatus.GATEWAY_TIMEOUT.value(),
                "Время ответа микросервиса истекло",
                "Попробуйте позже",
                LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .body(errorResponse);
    }

}


