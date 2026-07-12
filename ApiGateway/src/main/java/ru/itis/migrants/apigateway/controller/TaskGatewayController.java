package ru.itis.migrants.apigateway.controller;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.itis.migrants.apigateway.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.migrants.apigateway.dto.request.*;
import ru.itis.migrants.apigateway.dto.response.*;

import java.time.OffsetDateTime;


@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskGatewayController {

    private final TaskClient taskClient;
    private final UserClient userClient;
    private final DocumentClient documentClient;
    private final ContactClient contactClient;

    @PostExchange("/users")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody CreateUserRequest request
    ){
        return userClient.create(request);
    }

    @GetExchange("/users/{tg-id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("tg-id") Long tgId
    ) {
        return userClient.get(tgId);
    }

    @PostExchange("/tasks/{tg-id}")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable("tg-id") Long tgId,
            String title,
            OffsetDateTime deadline
    ) {
        if (deadline.isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Дедлайн не может быть в прошлом");
        }
        return  taskClient.create(tgId, title, deadline);
    }

    @PostExchange("/documents/{tg-id}")
    public ResponseEntity<DocumentResponse> createDocument(
            @PathVariable("tg-id")  Long tgId,
            @RequestBody CreateDocumentRequest request
    ) {
        return documentClient.create(tgId, request);
    }

    @PostExchange("/contacts/{tg-id}")
    public ResponseEntity<ContactResponse> createContact(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateContactRequest request
    ) {
        return contactClient.create(tgId, request);
    }

}

