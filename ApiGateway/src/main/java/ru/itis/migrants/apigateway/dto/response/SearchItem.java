package ru.itis.migrants.apigateway.dto.response;

import ru.itis.migrants.apigateway.dto.enums.SearchType;

public record SearchItem(
        SearchType type,
        String id,
        String title
) {
}
