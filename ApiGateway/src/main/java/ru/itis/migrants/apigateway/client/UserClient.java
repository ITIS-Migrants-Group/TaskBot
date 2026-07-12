package ru.itis.migrants.apigateway.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.itis.migrants.apigateway.dto.request.CreateUserRequest;
import ru.itis.migrants.apigateway.dto.response.UserResponse;

@Component
@HttpExchange("/api/v1/users")
public interface UserClient {

    /**
     * Создание нового пользователя
     */
    @PostExchange
    ResponseEntity<UserResponse> create(
            @RequestBody CreateUserRequest request
    );

    /**
     * Получение пользователя по Telegram ID
     */
    @GetExchange("/{tg-id}")
    ResponseEntity<UserResponse> get(
            @PathVariable("tg-id") Long tgId
    );
}