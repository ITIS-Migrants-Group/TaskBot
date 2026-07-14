package ru.itis.migrants.bot.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;

import ru.itis.migrants.bot.dto.request.*;
import ru.itis.migrants.bot.dto.response.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@HttpExchange("/api/v1")
public interface GatewayClient {

    @PostExchange("/users")
    ResponseEntity<UserResponse> createUser(
            @RequestBody CreateUserRequest request
    );

    @GetExchange("/users/{tg-id}")
    ResponseEntity<UserResponse> getUser(
            @PathVariable("tg-id") Long tgId
    );

    @PostExchange("/tasks/{tg-id}")
    ResponseEntity<TaskResponse> createTask(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateTaskRequest request
    );

    @GetExchange("/tasks/{tg-id}")
    ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable("tg-id") Long tgId,
            @RequestBody GetTasksRequest getTasksRequest
    );

    @DeleteExchange("/tasks/{tg-id}/{id}")
    ResponseEntity<Void> deleteTask(
            @PathVariable("tg-id") Long tgId,
            @PathVariable UUID id
    );

    @PostExchange("/documents/{tg-id}")
    ResponseEntity<DocumentResponse> createDocument(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateDocumentRequest request
    );

    @PostExchange("/contacts/{tg-id}")
    ResponseEntity<ContactResponse> createContact(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateContactRequest request
    );

    @PostExchange("/notifications/{tg-id}")
    ResponseEntity<NotificationResponse> createNotification(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateNotificationRequest request
    );

    @GetExchange("/{tg-id}/search")
    ResponseEntity<SearchResponse> search(
            @PathVariable("tg-id") Long tgId,
            @RequestParam String query
    );
}