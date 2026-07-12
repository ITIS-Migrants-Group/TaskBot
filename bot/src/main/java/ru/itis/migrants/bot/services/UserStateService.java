package ru.itis.migrants.bot.services;

import org.springframework.stereotype.Service;
import ru.itis.migrants.bot.models.ContactDialogData;
import ru.itis.migrants.bot.models.NotificationDialogData;
import ru.itis.migrants.bot.models.TaskDialogData;
import ru.itis.migrants.bot.models.enums.DialogState;
import ru.itis.migrants.bot.models.enums.NotifyPeriod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStateService {

    private final Map<Long, DialogState> states = new ConcurrentHashMap<>();
    private final Map<Long, TaskDialogData> taskDialogs = new ConcurrentHashMap<>();
    private final Map<Long, ContactDialogData> contactDialogs = new ConcurrentHashMap<>();
    private final Map<Long, NotificationDialogData> notificationDialogs = new ConcurrentHashMap<>();


    public DialogState getState(Long chatId) {
        return states.getOrDefault(chatId, DialogState.IDLE);
    }

    public void setState(Long chatId, DialogState state) {
        states.put(chatId, state);
    }

    public boolean isInDialog(Long chatId) {
        return getState(chatId) != DialogState.IDLE;
    }

    public void clearState(Long chatId) {
        states.remove(chatId);
    }

    public TaskDialogData getTaskDialog(Long chatId) {
        return taskDialogs.computeIfAbsent(chatId, k -> new TaskDialogData());
    }

    public void clearTaskDialog(Long chatId) {
        taskDialogs.remove(chatId);
    }

    public ContactDialogData getContactDialog(Long chatId) {
        return contactDialogs.computeIfAbsent(chatId, k -> new ContactDialogData());
    }

    public void clearContactDialog(Long chatId) {
        contactDialogs.remove(chatId);
    }

    public NotificationDialogData getNotificationDialog(Long chatId) {
        return notificationDialogs.computeIfAbsent(chatId, k -> new NotificationDialogData());
    }

    public void clearNotificationDialog(Long chatId) {
        notificationDialogs.remove(chatId);
    }

    public void clearAll(Long chatId) {
        clearState(chatId);
        clearTaskDialog(chatId);
        clearContactDialog(chatId);
    }
}
