package ru.itis.migrants.bot.models;

import lombok.Data;

@Data
public class NotificationDialogData {
    private String title;
    private String type;
    private String notifyAt;
    private String period;
}