package ru.itis.migrants.bot.commands.impl.task;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.client.GatewayClient;
import ru.itis.migrants.bot.commands.CallbackHandler;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteTaskCallbackHandler implements CallbackHandler {

    private final TelegramBot telegramBot;
    private final GatewayClient gatewayApi;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null &&
                update.callbackQuery().data() != null &&
                update.callbackQuery().data().startsWith("delete_task_");
    }

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        Long chatId = callbackQuery.from().id();

        String uuidStr = data.substring("delete_task_".length());
        try {
            UUID taskId = UUID.fromString(uuidStr);
            log.debug("Удаление задачи: chatId={}, taskId={}", chatId, taskId);
            gatewayApi.deleteTask(chatId, taskId);

            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id())
                    .text("✅ Задача удалена!")
                    .showAlert(false));

            telegramBot.execute(new SendMessage(chatId, "🗑 Задача с ID " + taskId + " удалена."));
        } catch (IllegalArgumentException e) {
            log.warn("Неверный UUID: {}", uuidStr);
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id())
                    .text("❌ Неверный ID задачи")
                    .showAlert(true));
        } catch (Exception e) {
            log.error("Ошибка при удалении задачи", e);
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id())
                    .text("❌ Ошибка удаления")
                    .showAlert(true));
        }
    }
}