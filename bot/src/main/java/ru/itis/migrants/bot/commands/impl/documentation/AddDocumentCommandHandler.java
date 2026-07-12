package ru.itis.migrants.bot.commands.impl.documentation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.api.DefaultApi;
import ru.itis.migrants.bot.commands.impl.AbstractCommandHandler;
import ru.itis.migrants.bot.model.CreateDocumentRequest;
import ru.itis.migrants.bot.model.Document;
import ru.itis.migrants.bot.models.enums.CommandType;

import static com.pengrad.telegrambot.utility.kotlin.extension.request.SendMessageExtensionKt.sendMessage;

@Component
public class AddDocumentCommandHandler extends AbstractCommandHandler {

    private final DefaultApi gatewayApi;

    public AddDocumentCommandHandler(TelegramBot telegramBot, DefaultApi gatewayApi) {
        super(telegramBot);
        this.gatewayApi = gatewayApi;
    }

    @Override
    public boolean supports(Update update) {
        if (validateUpdate(update)) {
            return false;
        }
        String text = update.message().text();
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
        Document documentResponse = gatewayApi.createDocument(Math.toIntExact(chatId),
                new CreateDocumentRequest().content(text.split(" ")[1])
        );
        StringBuilder response = new StringBuilder("Документ создан!\n");
        response.append("Содержание документа:").append(documentResponse.getContent()).append("\n");
        response.append("Время создания: ").append(documentResponse.getCreatedAt()).append("\n");
        sendMessageToUser(chatId, response.toString(), commandType.getType());
    }

}
