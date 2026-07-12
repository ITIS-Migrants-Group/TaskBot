package ru.itis.migrants.bot.commands.impl.todo;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.commands.DialogHandler;
import ru.itis.migrants.bot.models.UserDialogData;
import ru.itis.migrants.bot.models.enums.DialogState;
import ru.itis.migrants.bot.models.enums.NotifyPeriod;
import ru.itis.migrants.bot.services.UserStateService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static ru.itis.migrants.bot.models.enums.DialogState.AWAITING_NOTIFY_PERIOD;

@Component
@RequiredArgsConstructor
public class AddTodoDialogHandlerImpl implements DialogHandler {

    private final TelegramBot telegramBot;
    private final UserStateService userStateService;

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        if (message == null) return false;
        String text = message.text();
        if (text == null) return false;
        Long chatId = message.chat().id();

        if (text.equals("/addtask")) {
            return true;
        }

        if (userStateService.isInDialog(chatId) && !text.startsWith("/") || text.equals("/cancel")) {
            return true;
        }

        return false;
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Long chatId = message.chat().id();
        String text = message.text();

        if (text.equals("/addtask")) {
            userStateService.clear(chatId);
            UserDialogData data = userStateService.getUserData(chatId);
            data.setState(DialogState.AWAITING_TITLE);
            sendMessage(chatId, "Введите заголовок задачи:");
            return;
        }

        if (text.equals("/cancel")) {
            userStateService.clear(chatId);
            sendMessage(chatId, "Создание задачи отменено.");
            return;
        }

        UserDialogData data = userStateService.getUserData(chatId);
        DialogState currentState = data.getState();

        switch (currentState) {
            case AWAITING_TITLE:
                data.setTitle(text);
                data.setState(DialogState.AWAITING_DEADLINE);
                sendMessage(chatId, "Введите дедлайн в формате ISO 8601 (например, 2026-07-15T12:00:00+04:00):");
                break;

            case AWAITING_DEADLINE:
                try {
                    OffsetDateTime deadline = OffsetDateTime.parse(text);
                    if (deadline.isBefore(OffsetDateTime.now())) {
                        sendMessage(chatId, "Дедлайн должен быть в будущем. Попробуйте снова:");
                        return;
                    }
                    data.setDeadline(text);
                    data.setState(DialogState.AWAITING_NOTIFY_PERIOD);
                    sendPeriodSelection(chatId);
                } catch (DateTimeParseException e) {
                    sendMessage(chatId, "Неверный формат даты. Используйте формат ISO 8601 (например, 2026-07-15T12:00:00+04:00):");
                }
                break;

            case AWAITING_NOTIFY_PERIOD:

                String period = text.trim().toUpperCase();
                boolean valid = Arrays.stream(NotifyPeriod.values())
                        .anyMatch(p -> p.getIsoDuration().equals(period));
                if (!valid) {
                    sendMessage(chatId, "Некорректный период. Доступные варианты: " +
                            Arrays.toString(NotifyPeriod.values()) + ". Попробуйте снова:");
                    return;
                }
                data.setNotifyPeriod(period);
                createTask(chatId, data);
                break;

            default:
                sendMessage(chatId, "Произошла ошибка. Попробуйте начать заново с /addtask");
                userStateService.clear(chatId);
        }
    }

    private void sendPeriodSelection(Long chatId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<NotifyPeriod> periods = Arrays.asList(NotifyPeriod.values());
        for (NotifyPeriod period : periods) {
            markup.addRow(new InlineKeyboardButton(period.getDisplayName())
                    .callbackData("period_" + period.getIsoDuration()));
        }
        SendMessage sendMessage = new SendMessage(chatId, "Выберите время напоминания до дедлайна:");
        sendMessage.replyMarkup(markup);
        telegramBot.execute(sendMessage);
    }

    private void createTask(Long chatId, UserDialogData data) {
        try {
            TaskCreateDto createDto = new TaskCreateDto();
            createDto.setTitle(data.getTitle());
            createDto.setDeadline(OffsetDateTime.parse(data.getDeadline()));
            createDto.setNotifyFor(data.getNotifyPeriod());

            TaskResponseDto response = gatewayClient.createTask(chatId, createDto);

            sendMessage(chatId, "Задача успешно создана!\n" +
                    "Заголовок: " + response.getTitle() + "\n" +
                    "Дедлайн: " + response.getEndedAt() + "\n" +
                    "Напоминание установлено за " + data.getNotifyPeriod());
            userStateService.clear(chatId);

        } catch (Exception e) {
            log.error("Ошибка при создании задачи для чата {}", chatId, e);
            sendMessage(chatId, "Не удалось создать задачу. Попробуйте позже.");
            userStateService.clear(chatId);
        }
    }

    private void sendMessage(Long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }
}
