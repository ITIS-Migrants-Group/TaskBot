package ru.itis.migrants.bot.commands.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.models.enums.CommandType;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotCommandMenuInitializer {

    private final TelegramBot telegramBot;

    @PostConstruct
    public void setBotsMenuCommands() {
        List<BotCommand> menuCommands = Arrays.stream(CommandType.values())
                .filter(c -> !c.equals(CommandType.UNKNOWN))
                .map(type -> new BotCommand(type.getCommandTypeWithoutSlash(), type.getDescription()))
                .toList();
        log.atDebug()
                .addKeyValue("menuCommands", menuCommands)
                .log("Было инициализировано меню команд бота, чтобы пользователь видел спиок команд");
        telegramBot.execute(new SetMyCommands(menuCommands.toArray(new BotCommand[0])));
    }
}
