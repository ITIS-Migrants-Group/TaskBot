package ru.itis.migrants.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.migrants.notificationservice.dto.CreateNotificationRequest;
import ru.itis.migrants.notificationservice.dto.NotificationResponse;
import ru.itis.migrants.notificationservice.dto.UpdateNotificationRequest;
import ru.itis.migrants.notificationservice.service.NotificationService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;

    @PostMapping("{tg-id}")
    public ResponseEntity<NotificationResponse>
    createNotification(@PathVariable("tg-id") Long tgId,
                       @RequestBody CreateNotificationRequest request) {
        log.debug("Создание уведомления {}", request);
        NotificationResponse response = service.createNotification(tgId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<NotificationResponse>
    updateNotification(@RequestBody UpdateNotificationRequest request) {
        NotificationResponse response = service.updateNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
