package ru.itis.migrants.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.itis.migrants.apigateway.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.migrants.apigateway.dto.enums.SearchType;
import ru.itis.migrants.apigateway.dto.request.*;
import ru.itis.migrants.apigateway.dto.response.*;

import javax.management.DescriptorKey;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskGatewayController {

    private final TaskClient taskClient;
    private final UserClient userClient;
    private final DocumentClient documentClient;
    private final ContactClient contactClient;
    private final NotificationClient notificationClient;

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody CreateUserRequest request
    ){
        return userClient.create(request);
    }

    @GetMapping("/users/{tg-id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("tg-id") Long tgId
    ) {
        return userClient.get(tgId);
    }

    @PostMapping("/tasks/{tg-id}")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateTaskRequest request
    ) {
        log.debug("Создание задачи: {}", request);
        if (request.deadline().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Дедлайн не может быть в прошлом");
        }
        return taskClient.create(tgId, request);
    }

    @PostMapping("/documents/{tg-id}")
    public ResponseEntity<DocumentResponse> createDocument(
            @PathVariable("tg-id")  Long tgId,
            @RequestBody CreateDocumentRequest request
    ) {
        log.debug("Создание документа: {}", request);
        return documentClient.create(tgId, request);
    }

    @PostMapping("/contacts/{tg-id}")
    public ResponseEntity<ContactResponse> createContact(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateContactRequest request
    ) {
        log.debug("Создание контакта: {}", request);
        return contactClient.create(tgId, request);
    }

    @PostMapping("/notifications/{tg-id}")
    public ResponseEntity<NotificationResponse> createNotification(
        @PathVariable("tg-id") Long tgId,
        @RequestBody CreateNotificationRequest request
    ) {
        log.debug("Создание уведомления: {}", request);
        NotificationResponse response = notificationClient.create(tgId, request).getBody();
        log.debug("Ответ на создание уведомления: {}", response);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/tasks/{tg-id}")
    public ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable("tg-id") Long tgId,
            @RequestBody GetTasksRequest request
    ) {
        log.debug("Получения списка задач: {}", request);
        return taskClient.getAll(tgId, request);
    }

    @DeleteMapping("/tasks/{tg-id}/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("tg-id") Long tgId,
            @PathVariable UUID id
    ){
        log.debug("Удаление задачи с uuid={} пользователя={}", id, tgId);
        taskClient.deleteTask(tgId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/{tg-id}/search")
    public ResponseEntity<SearchResponse> search(
            @PathVariable("tg-id") Long tgId,
            @RequestParam("query") String query) {
        List<SearchItem> items = new ArrayList<>();

        try {
            ResponseEntity<List<ContactResponse>> contacts = contactClient.search(tgId,query);
            if (contacts.getBody() != null) {
                contacts.getBody().forEach(c ->
                        items.add(new SearchItem(SearchType.CONTACT, c.id().toString(), c.name()))
                );
            }
        } catch (Exception e) {
            log.warn("Ошибка поиска контактов: {}", e.getMessage());
        }

        try {
            ResponseEntity<List<DocumentResponse>> documents = documentClient.search(tgId, query);
            if (documents.getBody() != null) {
                documents.getBody().forEach(d ->
                        items.add(new SearchItem(SearchType.DOCUMENT, d.id().toString(), d.content()))
                );
            }
        } catch (Exception e) {
            log.warn("Ошибка поиска документов: {}", e.getMessage());
        }

        try {
            ResponseEntity<List<TaskResponse>> tasks = taskClient.search(tgId, query);
            if (tasks.getBody() != null) {
                tasks.getBody().forEach(t ->
                        items.add(new SearchItem(SearchType.TASK, t.id(), t.title()))
                );
            }
        } catch (Exception e) {
            log.warn("Ошибка поиска задач: {}", e.getMessage());
        }

        return ResponseEntity.ok(new SearchResponse(items, items.size()));
    }
}

