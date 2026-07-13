package ru.itis.migrants.bot.commands.impl.user;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.api.DefaultApi;
import ru.itis.migrants.bot.commands.impl.AbstractCommandHandler;
import ru.itis.migrants.bot.model.CreateUserRequest;
import ru.itis.migrants.bot.model.User;
import ru.itis.migrants.bot.models.enums.CommandType;

@Slf4j
@Component
public class StartCommandHandler extends AbstractCommandHandler {

    private final DefaultApi defaultApi;

    public StartCommandHandler(TelegramBot telegramBot, DefaultApi defaultApi) {
        super(telegramBot);
        this.defaultApi = defaultApi;
    }

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        if (message == null) {
            return false;
        }

        String text = message.text();
        if (text == null) {
            return false;
        }

        CommandType commandType = CommandType.getCommandTypeFromString(text.toLowerCase());
        return commandType == CommandType.START && commandType.isEnabled();
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        String text = message.text();
        Long chatId = message.chat().id();
        CommandType commandType = CommandType.START;
        setLogForRespondingToUser(chatId, text, commandType.getType());
        log.debug("Отправка в AG данные по команде /start");
        defaultApi.registerUser(new CreateUserRequest()
                .tgId(chatId)
                .username(message.chat().username())
        );

        sendMessageToUser(chatId, "Регистрация прошла успешно", "/start");
    }
}
