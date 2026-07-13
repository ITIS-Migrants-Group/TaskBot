package ru.itis.migrants.bot.commands.impl.search;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.api.DefaultApi;
import ru.itis.migrants.bot.commands.impl.AbstractCommandHandler;
import ru.itis.migrants.bot.model.SearchItem;
import ru.itis.migrants.bot.model.SearchResponse;
import ru.itis.migrants.bot.models.enums.CommandType;

@Component
@Slf4j
public class SearchCommandHandler extends AbstractCommandHandler {

    private final DefaultApi defaultApi;

    public SearchCommandHandler(TelegramBot telegramBot, DefaultApi defaultApi) {
        super(telegramBot);
        this.defaultApi = defaultApi;
    }

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        if (message == null) return false;
        String text = message.text();
        if (text == null || text.isBlank()) return false;
        return CommandType.getCommandTypeFromString(text) == CommandType.SEARCH;
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Long chatId = message.chat().id();
        String fullText = message.text();
        String commandType = CommandType.SEARCH.getType();
        String query = fullText.replaceFirst("(?i)/search\\s*", "").trim();
        if (query.isEmpty()) {
            sendMessageToUser(chatId, "ℹ️ Введите поисковый запрос: /search <текст>", commandType);
            return;
        }

        try {
            SearchResponse response = defaultApi.search(query);
            if (response.getItems() == null || response.getItems().isEmpty()) {
                sendMessageToUser(chatId, "🔍 Ничего не найдено по запросу: \"" + query + "\"", commandType);
                return;
            }

            StringBuilder result = new StringBuilder("🔎 Результаты поиска по \"" + query + "\":\n\n");
            for (SearchItem item : response.getItems()) {
                String type = switch (item.getType()) {
                    case TASK -> "Задача";
                    case CONTACT -> "Контакт";
                    case DOCUMENT -> "Документ";
                    default -> "•";
                };
                result.append(type)
                        .append(" [").append(item.getType()).append("] ")
                        .append(item.getTitle())
                        .append("\n(ID: ").append(item.getId()).append(")\n");
            }
            sendMessageToUser(chatId, result.toString(), commandType);
        } catch (Exception e) {
            log.error("Ошибка при поиске для чата {}", chatId, e);
            sendMessageToUser(chatId, "❌ Не удалось выполнить поиск. Попробуйте позже.", commandType);
        }
    }
}
