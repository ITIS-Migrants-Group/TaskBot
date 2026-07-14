package ru.itis.migrants.bot.dto.response;


import ru.itis.migrants.bot.dto.enums.SearchType;

public record SearchItem(
        SearchType type,
        String id,
        String title
) {
}
