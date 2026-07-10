package ru.itis.migrants.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.migrants.notificationservice.dto.CreateNotificationRequest;
import ru.itis.migrants.notificationservice.dto.NotificationResponse;
import ru.itis.migrants.notificationservice.service.NotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;

    @PostMapping("{tg-id}")
    public ResponseEntity<NotificationResponse>
    createNotification(@PathVariable("tg-id") Long tgId,
                       @RequestBody CreateNotificationRequest request) {
        NotificationResponse response = service.createNotification(tgId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
