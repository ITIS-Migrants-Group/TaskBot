package ru.itis.migrants.apigateway.client;

import org.springframework.stereotype.Component;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import ru.itis.migrants.apigateway.dto.request.CreateNotificationRequest;
import ru.itis.migrants.apigateway.dto.request.UpdateNotificationRequest;
import ru.itis.migrants.apigateway.dto.response.NotificationResponse;

@Component
@HttpExchange("/notifications")
public interface NotificationClient {

    @PostExchange("/{tg-id}")
    ResponseEntity<NotificationResponse> create(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateNotificationRequest request
    );

    @PutExchange
    ResponseEntity<NotificationResponse> update(
            @RequestBody UpdateNotificationRequest request
    );

    @GetExchange("/{notification-id}")
    ResponseEntity<NotificationResponse> get(
            @PathVariable("notification-id") Long notificationId
    );
}

