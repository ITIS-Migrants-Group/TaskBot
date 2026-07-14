package ru.itis.migrants.bot.commands.impl.task;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.client.GatewayClient;
import ru.itis.migrants.bot.commands.impl.AbstractCommandHandler;
import ru.itis.migrants.bot.dto.request.GetTasksRequest;
import ru.itis.migrants.bot.dto.response.TaskResponse;
import ru.itis.migrants.bot.models.enums.CommandType;
import ru.itis.migrants.bot.utils.DateTimeParser;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
public class GetListTasksCommandHandler extends AbstractCommandHandler {

    private final TelegramBot telegramBot;

    private final GatewayClient gatewayApi;

    private final DateTimeParser dateTimeParser;

    public GetListTasksCommandHandler(TelegramBot telegramBot, TelegramBot telegramBot1, GatewayClient gatewayApi, DateTimeParser dateTimeParser) {
        super(telegramBot);
        this.telegramBot = telegramBot1;
        this.gatewayApi = gatewayApi;
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        if (message == null) {
            return false;
        }

        String text = message.text();
        if (text == null || text.isBlank()) {
            return false;
        }
        CommandType commandType = CommandType.getCommandTypeFromString(text);
        return commandType == CommandType.TASKS && commandType.isEnabled();
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        String text = message.text();
        Long chatId = message.chat().id();
        CommandType commandType = CommandType.TASKS;
        setLogForRespondingToUser(chatId, text, commandType.getType());
        log.debug("Сообщение от пользователя: {}", text);
        String[] arg = text.trim().split("\\s+");
        List<TaskResponse> tasks;
        try {
            if (arg.length >= 4) {
                OffsetDateTime deadline = dateTimeParser.parseUserDate(
                        arg[2] + " " + arg[3]
                );
                log.debug(
                        "вызов метода со всеми параметрами: status={}, ended_at={}",
                        arg[1],
                        deadline
                );
                tasks = gatewayApi.getTasks(chatId, new GetTasksRequest(arg[1], deadline)).getBody();
            } else if (arg.length == 2) {
                if (containsNumber(arg[1])) {
                    OffsetDateTime deadline = dateTimeParser.parseUserDate(arg[1]);
                    tasks = gatewayApi.getTasks(chatId, new GetTasksRequest(null, deadline)).getBody();
                } else {
                    tasks = gatewayApi.getTasks(chatId, new GetTasksRequest(arg[1], null)).getBody();
                }
            } else {
                tasks = gatewayApi.getTasks(chatId, new GetTasksRequest(null, null)).getBody();
            }
            sendTaskList(chatId, tasks);
        } catch (Exception e) {
            log.error("Ошибка обработки команды {}", text, e);
            sendMessageToUser(
                    chatId,
                    "❌ Ошибка получения задач: " + e.getMessage(),
                    commandType.getType()
            );
        }
    }

    private void sendTaskList(Long chatId, List<TaskResponse> tasks) {
        if (tasks.isEmpty()) {
            sendMessageToUser(chatId, "📭 Задачи не найдены.", CommandType.TASKS.getType());
            return;
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        StringBuilder sb = new StringBuilder("📋 Список задач:\n\n");
        for (TaskResponse task : tasks) {
            sb.append("📌 Название: ").append(task.title()).append("\n");
            sb.append("📊 Статус: ").append(task.status()).append("\n");
            sb.append("⏳ Дедлайн: ").append(dateTimeParser.formatForUser(task.endedAt())).append("\n");
            sb.append("───────────────────\n");
            String callbackData = "delete_task_" + task.id().toString();
            markup.addRow(new InlineKeyboardButton("🗑 Удалить")
                    .callbackData(callbackData));
        }

        SendMessage sendMessage = new SendMessage(chatId, sb.toString());
        sendMessage.replyMarkup(markup);
        telegramBot.execute(sendMessage);
    }

    public static boolean containsNumber(String str) {
        return str != null && str.matches(".*\\d.*");
    }

}
