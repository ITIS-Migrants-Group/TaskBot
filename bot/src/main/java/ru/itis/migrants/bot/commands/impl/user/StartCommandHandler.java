package ru.itis.migrants.bot.commands.impl.user;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.client.GatewayClient;
import ru.itis.migrants.bot.commands.impl.AbstractCommandHandler;
import ru.itis.migrants.bot.dto.request.CreateUserRequest;
import ru.itis.migrants.bot.dto.response.UserResponse;
import ru.itis.migrants.bot.models.enums.CommandType;

@Slf4j
@Component
public class StartCommandHandler extends AbstractCommandHandler {

    private final GatewayClient defaultApi;

    public StartCommandHandler(TelegramBot telegramBot, GatewayClient defaultApi) {
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
        UserResponse userResponse = defaultApi.createUser(
                new CreateUserRequest(chatId, message.chat().username())
        ).getBody();

        sendMessageToUser(chatId, "Регистрация пользователя с id=" + userResponse.tgId() + " прошла успешно", "/start");
    }
}
