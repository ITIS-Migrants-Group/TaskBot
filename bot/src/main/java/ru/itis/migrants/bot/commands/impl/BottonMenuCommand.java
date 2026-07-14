package ru.itis.migrants.bot.commands.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.models.enums.CommandType;
import ru.itis.migrants.bot.services.BotKeyboardService;

@Component
public class BottonMenuCommand extends AbstractCommandHandler {

    private final BotKeyboardService botKeyboardService;

    public BottonMenuCommand(TelegramBot telegramBot, BotKeyboardService botKeyboardService) {
        super(telegramBot);
        this.botKeyboardService = botKeyboardService;
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
        return commandType == CommandType.MENU && commandType.isEnabled();
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Long chatId = message.chat().id();
        botKeyboardService.sendMainKeyboard(chatId);
        super.handle(update);
    }
}
