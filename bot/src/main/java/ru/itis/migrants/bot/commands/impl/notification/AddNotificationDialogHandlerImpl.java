package ru.itis.migrants.bot.commands.impl.notification;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.client.GatewayClient;
import ru.itis.migrants.bot.commands.DialogHandler;

import ru.itis.migrants.bot.dto.enums.NotificationType;
import ru.itis.migrants.bot.dto.request.CreateNotificationRequest;
import ru.itis.migrants.bot.dto.response.NotificationResponse;
import ru.itis.migrants.bot.models.NotificationDialogData;
import ru.itis.migrants.bot.models.enums.DialogState;
import ru.itis.migrants.bot.services.UserStateService;
import ru.itis.migrants.bot.utils.DateTimeParser;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddNotificationDialogHandlerImpl implements DialogHandler {

    private final TelegramBot telegramBot;
    private final UserStateService userStateService;
    private final GatewayClient defaultApi;
    private final DateTimeParser dateTimeParser;

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
                sendMessage(chatId, "Введите время дедлайна в формате dd:MM:yyyy HH:mm (например 15:07:2026 12:00):");
                break;

            case AWAITING_NOTIFICATION_DATETIME:
                try {
                    log.debug("Время от пользователя: {}", text);
                    OffsetDateTime notifyAt = dateTimeParser.parseUserDate(text);
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
                    sendMessage(chatId, "Неверный формат даты. Введите время дедлайна в формате dd:MM:yyyy HH:mm (например 15:07:2026 12:00):");
                }
                break;

            case AWAITING_NOTIFICATION_INTERVAL:
                try {
                    Duration period = Duration.parse(text);
                    data.setPeriod(period);
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
            String title = null;
            if (data.getTitle() != null) {
                title = data.getTitle();
            }
            Duration period = null;
            if (data.getPeriod() != null) {
                period = data.getPeriod();
            }
            CreateNotificationRequest request = new CreateNotificationRequest(
                    title,
                    null,
                    NotificationType.valueOf(data.getType()),
                    data.getNotifyAt(),
                    period
            );
            log.debug("отпарвка уведомления: {}", request);
            NotificationResponse notification = defaultApi.createNotification(chatId, request).getBody();

            StringBuilder response = new StringBuilder("✅ Уведомление создано!\n");
            response.append("Заголовок: ").append(notification.title()).append("\n");
            response.append("Тип: ").append(notification.type()).append("\n");
            response.append("Время: ").append(dateTimeParser.formatForUser(notification.notifyAt())).append("\n");
            if (notification.period() != null) {
                response.append("Период: ").append(notification.period()).append("\n");
            }
            response.append("Активно: ").append(notification.isActive());

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
