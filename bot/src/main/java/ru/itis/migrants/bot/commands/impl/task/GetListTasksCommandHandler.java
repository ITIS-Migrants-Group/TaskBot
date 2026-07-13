package ru.itis.migrants.bot.commands.impl.task;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.api.DefaultApi;
import ru.itis.migrants.bot.commands.impl.AbstractCommandHandler;
import ru.itis.migrants.bot.model.Task;
import ru.itis.migrants.bot.models.enums.CommandType;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
public class GetListTasksCommandHandler extends AbstractCommandHandler {

    private final TelegramBot telegramBot;

    private final DefaultApi gatewayApi;

    public GetListTasksCommandHandler(TelegramBot telegramBot, TelegramBot telegramBot1, DefaultApi gatewayApi) {
        super(telegramBot);
        this.telegramBot = telegramBot1;
        this.gatewayApi = gatewayApi;
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
        String[] arg = text.split(" ");
        List<Task> tasks;
        try {
            if (arg.length == 2) {
                if (containsNumber(arg[1])) {
                    OffsetDateTime deadline = OffsetDateTime.parse(arg[1]);
                    tasks = gatewayApi.getTasks(chatId, null, deadline);
                } else {
                    tasks = gatewayApi.getTasks(chatId, arg[1], null);
                }
            } else if (arg.length == 3) {
                OffsetDateTime deadline = OffsetDateTime.parse(arg[2]);
                tasks = gatewayApi.getTasks(chatId, arg[1], deadline);
            } else {
                tasks = gatewayApi.getTasks(chatId, null, null);
            }
            sendTaskList(chatId, tasks);
        } catch (Exception e) {
            sendMessageToUser(chatId, "❌ Ошибка получения задач: " + e.getMessage(), commandType.getType());
        }
    }

    private void sendTaskList(Long chatId, List<Task> tasks) {
        if (tasks.isEmpty()) {
            sendMessageToUser(chatId, "📭 Задачи не найдены.", CommandType.TASKS.getType());
            return;
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        StringBuilder sb = new StringBuilder("📋 Список задач:\n\n");
        for (Task task : tasks) {
            sb.append("🆔 ID: ").append(task.getId()).append("\n");
            sb.append("📌 Название: ").append(task.getTitle()).append("\n");
            sb.append("📊 Статус: ").append(task.getStatus()).append("\n");
            sb.append("⏳ Дедлайн: ").append(task.getAtEnded()).append("\n");
            sb.append("───────────────────\n");
            String callbackData = "delete_task_" + task.getId().toString();
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
