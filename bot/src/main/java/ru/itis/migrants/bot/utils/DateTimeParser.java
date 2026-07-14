package ru.itis.migrants.bot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeParser {

    private static final DateTimeFormatter USER_FORMAT =
            DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm");

    @Value("${app.timezone}")
    private String timezone;

    public OffsetDateTime parseUserDate(String value) {
        LocalDateTime localDateTime =
                LocalDateTime.parse(value, USER_FORMAT);

        return localDateTime
                .atZone(ZoneId.of(timezone))
                .toOffsetDateTime();
    }

    public String formatForUser(OffsetDateTime value) {
        return value
                .atZoneSameInstant(ZoneId.of(timezone))
                .format(USER_FORMAT);
    }

    public boolean isPast(OffsetDateTime value) {
        return value.isBefore(OffsetDateTime.now());
    }

}
