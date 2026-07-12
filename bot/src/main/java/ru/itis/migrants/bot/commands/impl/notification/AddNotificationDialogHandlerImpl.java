package ru.itis.migrants.bot.commands.impl.notification;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.commands.impl.AbstractCommandHandler;
import ru.itis.migrants.bot.models.enums.CommandType;

@Component
public class AddNotificationCommandHandler extends AbstractCommandHandler {

    public AddNotificationCommandHandler(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public boolean supports(Update update) {
        if (validateUpdate(update)) {
            return false;
        }
        String text = update.message().text();
        CommandType commandType = CommandType.getCommandTypeFromString(text);

        return commandType.equals(CommandType.ADDNOTIFICATION) && commandType.isEnabled();
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        String text = message.text();
        Long chatId = message.chat().id();
        CommandType commandType = CommandType.ADDNOTIFICATION;
        setLogForRespondingToUser(chatId, text, commandType.getType());
    }
}
