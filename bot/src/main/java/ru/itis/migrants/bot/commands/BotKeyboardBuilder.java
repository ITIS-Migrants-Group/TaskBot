package ru.itis.migrants.bot.commands;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.models.enums.CommandType;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BotKeyboardBuilder {

    public ReplyKeyboardMarkup buildMainKeyboard() {
        Map<Integer, List<KeyboardButton>> rows =
                Arrays.stream(CommandType.values())
                        .filter(CommandType::isEnabled)
                        .filter(c -> c.getKeyboardRow() >= 0)
                        .collect(Collectors.groupingBy(
                                CommandType::getKeyboardRow,
                                LinkedHashMap::new,
                                Collectors.mapping(
                                        this::convert,
                                        Collectors.toList()
                                )
                        ));
        KeyboardButton[][] keyboard =
                rows.values()
                        .stream()
                        .map(list -> list.toArray(new KeyboardButton[0]))
                        .toArray(KeyboardButton[][]::new);
        ReplyKeyboardMarkup markup =
                new ReplyKeyboardMarkup(keyboard);
        markup.resizeKeyboard(true);
        markup.oneTimeKeyboard(false);
        return markup;
    }

    private KeyboardButton convert(CommandType command) {
        return new KeyboardButton(
                command.getButtonText()
        );
    }
}
