package ru.itis.migrants.apigateway.dto.response;

import java.util.List;

public record SearchResponse(
        List<SearchItem> items,
        int size
) {
}
