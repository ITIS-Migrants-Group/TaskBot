package ru.itis.migrants.bot.commands.impl.notification;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.api.DefaultApi;
import ru.itis.migrants.bot.commands.DialogHandler;

import ru.itis.migrants.bot.model.CreateNotificationRequest;
import ru.itis.migrants.bot.model.Notification;
import ru.itis.migrants.bot.models.NotificationDialogData;
import ru.itis.migrants.bot.models.enums.DialogState;
import ru.itis.migrants.bot.services.UserStateService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddNotificationDialogHandlerImpl implements DialogHandler {

    private final TelegramBot telegramBot;
    private final UserStateService userStateService;
    private final DefaultApi defaultApi;

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        if (message == null) return false;
        String text = message.text();
        if (text == null) return false;
        Long chatId = message.chat().id();

        if (text.equals("/addnotification")) {
            return true;
        }

        if (text.equals("/cancel")) {
            return userStateService.isInDialog(chatId);
        }

        DialogState state = userStateService.getState(chatId);
        return state == DialogState.AWAITING_NOTIFICATION_TITLE ||
                state == DialogState.AWAITING_NOTIFICATION_TYPE ||
                state == DialogState.AWAITING_NOTIFICATION_DATETIME ||
                state == DialogState.AWAITING_NOTIFICATION_INTERVAL;
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Long chatId = message.chat().id();
        String text = message.text();

        if (text.equals("/addnotification")) {
            userStateService.clearAll(chatId);
            userStateService.setState(chatId, DialogState.AWAITING_NOTIFICATION_TITLE);
            sendMessage(chatId, "Введите заголовок уведомления (или /skip):");
            return;
        }

        if (text.equals("/cancel")) {
            userStateService.clearAll(chatId);
            sendMessage(chatId, "Создание уведомления отменено.");
            return;
        }

        DialogState currentState = userStateService.getState(chatId);
        NotificationDialogData data = userStateService.getNotificationDialog(chatId);

        switch (currentState) {
            case AWAITING_NOTIFICATION_TITLE:
                if (!text.equals("/skip")) {
                    data.setTitle(text.trim());
                }
                userStateService.setState(chatId, DialogState.AWAITING_NOTIFICATION_TYPE);
                sendMessage(chatId, "Введите тип уведомления (ONCE или PERIOD):");
                break;

            case AWAITING_NOTIFICATION_TYPE:
                String type = text.trim().toUpperCase();
                if (!type.equals("ONCE") && !type.equals("PERIOD")) {
                    sendMessage(chatId, "Неверный тип. Введите ONCE или PERIOD:");
                    return;
                }
                data.setType(type);
                userStateService.setState(chatId, DialogState.AWAITING_NOTIFICATION_DATETIME);
                sendMessage(chatId, "Введите вермя дедлайна в формате ISO 8601 (например, 2026-07-15T12:00:00+04:00):");
                break;

            case AWAITING_NOTIFICATION_DATETIME:
                try {
                    OffsetDateTime notifyAt = OffsetDateTime.parse(text);
                    if (notifyAt.isBefore(OffsetDateTime.now())) {
                        sendMessage(chatId, "Дата должна быть в будущем. Попробуйте снова:");
                        return;
                    }
                    data.setNotifyAt(notifyAt);
                    if (data.getType().equals("PERIOD")) {
                        userStateService.setState(chatId, DialogState.AWAITING_NOTIFICATION_INTERVAL);
                        sendMessage(chatId, "Введите период в формате ISO 8601 (например, PT1H для каждого часа):");
                    } else {
                        createNotification(chatId, data);
                    }
                } catch (DateTimeParseException e) {
                    sendMessage(chatId, "Неверный формат даты. Используйте ISO 8601 (например, 2026-07-15T12:00:00+04:00):");
                }
                break;

            case AWAITING_NOTIFICATION_INTERVAL:
                try {
                    java.time.Duration.parse(text);
                    data.setPeriod(text);
                    createNotification(chatId, data);
                } catch (DateTimeParseException e) {
                    sendMessage(chatId, "Неверный формат периода. Используйте, например, PT1H (1 час), PT30M (30 минут):");
                }
                break;

            default:
                sendMessage(chatId, "Произошла ошибка. Попробуйте начать заново с /addnotification");
                userStateService.clearAll(chatId);
        }
    }

    private void createNotification(Long chatId, NotificationDialogData data) {
        try {
            CreateNotificationRequest request = new CreateNotificationRequest();
            if (data.getTitle() != null) {
                request.setTitle(data.getTitle());
            }
            request.setType(CreateNotificationRequest.TypeEnum.valueOf(data.getType()));
            request.setNotifyAt(data.getNotifyAt());
            if (data.getPeriod() != null) {
                request.setPeriod(data.getPeriod());
            }
            log.debug("отпарвка уведомления: {}", request);
            Notification notification = defaultApi.createNotification(chatId, request);

            StringBuilder response = new StringBuilder("✅ Уведомление создано!\n");
            response.append("Заголовок: ").append(notification.getTitle()).append("\n");
            response.append("Тип: ").append(notification.getType()).append("\n");
            response.append("Время: ").append(notification.getNotifyAt()).append("\n");
            if (notification.getPeriod() != null) {
                response.append("Период: ").append(notification.getPeriod()).append("\n");
            }
            response.append("Активно: ").append(notification.getIsActive());

            sendMessage(chatId, response.toString());
            userStateService.clearAll(chatId);

        } catch (Exception e) {
            log.error("Ошибка при создании уведомления для чата {}", chatId, e);
            sendMessage(chatId, "❌ Не удалось создать уведомление. Попробуйте позже.");
            userStateService.clearAll(chatId);
        }
    }

    private void sendMessage(Long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }


}
