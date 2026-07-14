package ru.itis.migrants.bot.models;

import lombok.Data;

import java.time.Duration;
import java.time.OffsetDateTime;

@Data
public class NotificationDialogData {
    private String title;
    private String type;
    private OffsetDateTime notifyAt;
    private Duration period;
}