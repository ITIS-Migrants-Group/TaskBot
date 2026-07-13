package ru.itis.migrants.bot.commands;

import com.pengrad.telegrambot.model.Update;

public interface CallbackHandler {
    boolean supports(Update update);

    void handle(Update update);
}
