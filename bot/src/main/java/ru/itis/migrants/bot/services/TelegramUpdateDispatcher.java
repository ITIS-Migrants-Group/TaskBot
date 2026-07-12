package ru.itis.migrants.bot.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramUpdateDispatcher {

    private final List<CommandHandler> commandHandlers;

    public void dispatch(Update update) {
        if (update == null) {
            return;
        }

        for (CommandHandler handler : commandHandlers) {
            if (handler.supports(update)) {
                handler.handle(update);
                break;
            }
        }
    }
}
