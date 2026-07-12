package backend.academy.linktracker.bot.components.command.handlers;

import com.pengrad.telegrambot.model.Update;

public interface CommandHandler {

    boolean supports(Update update);

    void handle(Update update);
}
