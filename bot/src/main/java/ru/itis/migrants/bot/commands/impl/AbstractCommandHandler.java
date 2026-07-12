package ru.itis.migrants.bot.commands.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import ru.itis.migrants.bot.commands.CommandHandler;

@Slf4j
public class AbstractCommandHandler implements CommandHandler {

    private final TelegramBot telegramBot;

    public AbstractCommandHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public boolean supports(Update update) {
        return false;
    }

    @Override
    public void handle(Update update) {}

    protected boolean validateUpdate(Update update) {
        Message message = update.message();
        if (message == null) {
            return false;
        }

        String text = message.text();
        return text != null && !text.isBlank();
    }

    protected void sendMessageToUser(long chatId, String messageForClient, String command) {
        log.atDebug()
                .addKeyValue("chatId", chatId)
                .addKeyValue("responseText", messageForClient)
                .log("Отправлен ответ пользователю на команду {}", command);

        SendMessage sentMessage = new SendMessage(chatId, messageForClient);
        telegramBot.execute(sentMessage);
    }

    protected void setLogForRespondingToUser(Long chatId, String messageText, String commandType) {
        log.atDebug()
                .addKeyValue("chatId", chatId)
                .addKeyValue("messageText", messageText)
                .addKeyValue("commandType", commandType)
                .log("Получено сообщение от пользователя с командой {}", commandType);
    }
}
