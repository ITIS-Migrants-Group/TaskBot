package ru.itis.migrants.bot.listeners;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.migrants.bot.services.TelegramUpdateDispatcher;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramBotListener implements UpdatesListener {

    private final TelegramBot telegramBot;

    private final TelegramUpdateDispatcher dispatcher;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            try {
                if (update.message() != null) {
                    dispatcher.dispatch(update);
                }
            } catch (Exception e) {
                log.error("Ошибка в процессе обработки update: {}", update, e);
            }
        }
        return CONFIRMED_UPDATES_ALL;
    }
}
