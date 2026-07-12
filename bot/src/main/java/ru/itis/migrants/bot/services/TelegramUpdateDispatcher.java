package ru.itis.migrants.bot.services;

import com.pengrad.telegrambot.model.Update;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.migrants.bot.commands.CallbackHandler;
import ru.itis.migrants.bot.commands.CommandHandler;
import ru.itis.migrants.bot.commands.DialogHandler;

@Service
@RequiredArgsConstructor
public class TelegramUpdateDispatcher {

    private final List<DialogHandler> dialogHandlers;

    private final List<CommandHandler> commandHandlers;

    private final List<CallbackHandler> callbackHandlers;

    public void dispatch(Update update) {
        if (update == null) {
            return;
        }

        for (CallbackHandler handler : callbackHandlers) {
            if (handler.supports(update)) {
                handler.handle(update);
                break;
            }
        }

        for (DialogHandler dialogHandler : dialogHandlers) {
            if (dialogHandler.supports(update)) {
                dialogHandler.handle(update);
                break;
            }
        }

        for (CommandHandler handler : commandHandlers) {
            if (handler.supports(update)) {
                handler.handle(update);
                break;
            }
        }
    }
}
