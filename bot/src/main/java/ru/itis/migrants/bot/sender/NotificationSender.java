package ru.itis.migrants.bot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.dto.NotificationResponse;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSender {
    private final TelegramBot bot;

    public void send(NotificationResponse response) {
        try {
            Optional<String> message = buildMessage(response);
            message.ifPresent(string -> bot.execute(new SendMessage(response.ownerId(), string)));
        } catch (NullPointerException ex) {
            log.error("ERROR: sender catch NPE");
        }
    }

    public Optional<String> buildMessage(NotificationResponse response) {
        if (!response.isActive()) {
            return Optional.empty();
        }
        StringBuilder builder = new StringBuilder("Новое уведомление: \n");
        if (response.taskId() != null) {
            builder.append(String.format(" приближается дедлайн задачи: %s", response.title()));
        } else {
            builder.append(String.format("Приближается время завершения простого уведомления: \n%s", response.title()));
        }
        return Optional.of(builder.toString());
    }
}
