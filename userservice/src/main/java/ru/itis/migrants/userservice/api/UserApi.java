package ru.itis.migrants.userservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import ru.itis.migrants.userservice.dto.request.UserRequest;
import ru.itis.migrants.userservice.dto.response.UserResponse;


@Tag(name = "Users | Пользователи", description = "API для работы с пользователями")
@RequestMapping("/api/v1/users")
public interface UserApi {

    @Operation(summary = "Регистрация нового пользователя (через Telegram)",
            description = "Создаёт и возвращает пользователя с минимальным набором данных. Роль по умолчанию — ROLE_USER",
            operationId = "user-create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким tg-id уже существует")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse create(@RequestBody UserRequest request);

    @Operation(summary = "Получение пользователя по ID",
            description = "Возвращает пользователя по tg-id",
            operationId = "user-get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{tg-id}")
    @ResponseStatus(HttpStatus.OK)
    UserResponse getById(@PathVariable("tg-id") Long tgId);

    @Operation(summary = "Удаление пользователя (только для администратора) по ID",
            operationId = "user-delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удалён", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{tg-id}")
    @ResponseStatus(HttpStatus.OK)
    UserResponse deleteById(@PathVariable("tg-id") Long tgId);
}
