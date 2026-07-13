package ru.itis.migrants.bot.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotifyPeriod {
    PT15M("PT15M", "15 минут"),
    PT30M("PT30M", "30 минут"),
    PT1H("PT1H", "1 час"),
    PT2H("PT2H", "2 часа"),
    PT1D("PT1D", "1 день");

    private final String isoDuration;
    private final String displayName;

}
