package ru.itis.migrants.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.migrants.bot.commands.BotKeyboardBuilder;

@Service
@RequiredArgsConstructor
public class BotKeyboardService {

    private final TelegramBot telegramBot;
    private final BotKeyboardBuilder keyboardBuilder;


    public void sendMainKeyboard(Long chatId) {
        SendMessage message = new SendMessage(
                chatId,
                "Выберите действие:"
        );

        ReplyKeyboardMarkup keyboard =
                keyboardBuilder.buildMainKeyboard();

        message.replyMarkup(keyboard);

        telegramBot.execute(message);
    }
}