package ru.itis.migrants.bot.commands.impl.todo;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.commands.impl.AbstractCommandHandler;
import ru.itis.migrants.bot.models.enums.CommandType;

@Component
public class GetListTasksCommandHandler extends AbstractCommandHandler {

    private final TodoServiceApi todoServiceApi;

    public GetListTasksCommandHandler(TelegramBot telegramBot) {
        super(telegramBot);
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
        String[] arg = text.split(" ");

        //todo:реализовать отправку
        if (arg.length == 2) {

        } else if (arg.length == 3) {

        } else {
            sendMessageToUser(chatId, "Много аргументов для команды " + commandType, commandType.getType());
        }
    }


}
