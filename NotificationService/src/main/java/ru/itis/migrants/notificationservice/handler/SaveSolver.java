package ru.itis.migrants.notificationservice.handler;

import ru.itis.migrants.notificationservice.dto.NotificationResponse;

import java.time.OffsetDateTime;

public interface SaveSolver {
    boolean solve(NotificationResponse response, OffsetDateTime offsetDateTime);
    boolean support(NotificationResponse response);
}
