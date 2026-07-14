package ru.itis.migrants.bot.commands.impl.documentation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.client.GatewayClient;
import ru.itis.migrants.bot.commands.impl.AbstractCommandHandler;
import ru.itis.migrants.bot.dto.request.CreateDocumentRequest;
import ru.itis.migrants.bot.dto.response.DocumentResponse;
import ru.itis.migrants.bot.models.enums.CommandType;

import java.time.OffsetDateTime;


@Slf4j
@Component
public class AddDocumentCommandHandler extends AbstractCommandHandler {

    private final GatewayClient gatewayApi;

    public AddDocumentCommandHandler(TelegramBot telegramBot, GatewayClient gatewayApi) {
        super(telegramBot);
        this.gatewayApi = gatewayApi;
    }

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        if (message == null) return false;
        String text = message.text();
        if (text == null) return false;
        CommandType commandType = CommandType.getCommandTypeFromString(text);

        return commandType.equals(CommandType.ADDDOCUMENT) && commandType.isEnabled();
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        String text = message.text();
        Long chatId = message.chat().id();
        CommandType commandType = CommandType.ADDDOCUMENT;
        setLogForRespondingToUser(chatId, text, commandType.getType());
        log.debug("Создание документа");
        DocumentResponse documentResponse = gatewayApi.createDocument(chatId,
                new CreateDocumentRequest(text.split("/adddocument")[1])
        ).getBody();
        StringBuilder response = new StringBuilder("Документ создан!\n");
        response.append("Содержание документа:").append(documentResponse.content()).append("\n");
        response.append("Время создания: ").append(OffsetDateTime.now()).append("\n");
        sendMessageToUser(chatId, response.toString(), commandType.getType());
    }

}
