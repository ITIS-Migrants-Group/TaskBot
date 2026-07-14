package ru.itis.migrants.bot.commands.impl.task;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.api.DefaultApi;
import ru.itis.migrants.bot.commands.DialogHandler;
import ru.itis.migrants.bot.model.CreateTaskRequest;
import ru.itis.migrants.bot.model.Task;
import ru.itis.migrants.bot.models.TaskDialogData;
import ru.itis.migrants.bot.models.enums.DialogState;
import ru.itis.migrants.bot.models.enums.NotifyPeriod;
import ru.itis.migrants.bot.services.UserStateService;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddTaskDialogHandlerImpl implements DialogHandler {

    private final TelegramBot telegramBot;
    private final UserStateService userStateService;
    private final DefaultApi gatewayClient;

    @Override
    public boolean supports(Update update) {
        if (update.callbackQuery() != null) {
            String data = update.callbackQuery().data();
            if (data != null && data.startsWith("period_")) {
                Long chatId = update.callbackQuery().message().chat().id();
                return userStateService.isInDialog(chatId);
            }
            return false;
        }

        Message message = update.message();
        if (message == null) return false;
        String text = message.text();
        if (text == null) return false;
        Long chatId = message.chat().id();

        if (text.equals("/addtask")) {
            return true;
        }
        if (text.equals("/cancel")) {
            return userStateService.isInDialog(chatId);
        }

        DialogState state = userStateService.getState(chatId);
        return state == DialogState.AWAITING_TITLE ||
                state == DialogState.AWAITING_DEADLINE ||
                state == DialogState.AWAITING_NOTIFY_PERIOD;
    }

    @Override
    public void handle(Update update) {
        if (update.callbackQuery() != null) {
            String data = update.callbackQuery().data();
            if (data != null && data.startsWith("period_")) {
                String period = data.substring("period_".length());
                Long chatId = update.callbackQuery().message().chat().id();

                TaskDialogData dialogData = userStateService.getTaskDialog(chatId);
                dialogData.setNotifyPeriod(period);
                createTask(chatId, dialogData);

                telegramBot.execute(new AnswerCallbackQuery(update.callbackQuery().id()));
                return;
            }
        }

        Message message = update.message();
        if (message == null) return;
        Long chatId = message.chat().id();
        String text = message.text();

        if (text.equals("/addtask")) {
            userStateService.clearTaskDialog(chatId);
            userStateService.clearState(chatId);
            TaskDialogData data = userStateService.getTaskDialog(chatId);
            data.setState(DialogState.AWAITING_TITLE);
            userStateService.setState(chatId, DialogState.AWAITING_TITLE);
            sendMessage(chatId, "Введите заголовок задачи:");
            return;
        }

        if (text.equals("/cancel")) {
            userStateService.clearTaskDialog(chatId);
            userStateService.clearState(chatId);
            sendMessage(chatId, "Создание задачи отменено.");
            return;
        }

        TaskDialogData data = userStateService.getTaskDialog(chatId);
        DialogState currentState = data.getState();

        switch (currentState) {
            case AWAITING_TITLE:
                data.setTitle(text);
                data.setState(DialogState.AWAITING_DEADLINE);
                userStateService.setState(chatId, DialogState.AWAITING_DEADLINE);
                sendMessage(chatId, "Введите дедлайн в формате dd.mm.YYYY hh:mm:");
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
                    userStateService.setState(chatId, DialogState.AWAITING_NOTIFY_PERIOD);
                    sendPeriodSelection(chatId);
                } catch (DateTimeParseException e) {
                    sendMessage(chatId, "Неверный формат даты. Используйте формат dd.mm.YYYY hh:mm:");
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
                userStateService.clearTaskDialog(chatId);
                userStateService.clearState(chatId);
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

    private void createTask(Long chatId, TaskDialogData data) {
        try {
            LocalDateTime localTime = LocalDateTime.parse(data.getDeadline());
            OffsetDateTime parsedTime = OffsetDateTime.of(localTime, ZoneOffset.ofHours(3));

            CreateTaskRequest request = new CreateTaskRequest()
                    .deadline()
                    .title(data.getTitle())
                    .notifyFor(parsedTime);

            Task task = gatewayClient.createTask(chatId, request);
            sendMessage(chatId, "Задача успешно создана!\n" +
                    "Заголовок: " + task.getTitle() + "\n" +
                    "Дедлайн: " + request.getDeadline() + "\n" +
                    "Напоминание установлено за " + data.getNotifyPeriod());
            userStateService.clearTaskDialog(chatId);
            userStateService.clearState(chatId);

        } catch (Exception e) {
            log.error("Ошибка при создании задачи для чата {}", chatId, e);
            sendMessage(chatId, "Не удалось создать задачу. Попробуйте позже.");
            userStateService.clearTaskDialog(chatId);
            userStateService.clearState(chatId);
        }
    }

    private void sendMessage(Long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }
}
