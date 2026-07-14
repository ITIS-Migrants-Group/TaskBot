package ru.itis.migrants.bot.dto.response;

import java.util.List;

public record SearchResponse(
        List<SearchItem> items,
        int size
) {
}
