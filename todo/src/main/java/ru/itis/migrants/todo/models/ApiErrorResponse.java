package ru.itis.migrants.todo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorResponse {

    private int statusCode;

    private String description;

    private String exceptionName;

    private String exceptionMessage;
}
