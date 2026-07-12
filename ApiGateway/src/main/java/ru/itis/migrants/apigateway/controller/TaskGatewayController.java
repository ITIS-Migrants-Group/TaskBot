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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @PostExchange("/notifications/{tg-id}")
    public ResponseEntity<NotificationResponse> createNotification(
        @PathVariable("tg-id") Long tgId,
            @RequestBody CreateNotificationRequest request
    ) {
        return notificationClient.create(tgId, request);
    }

    @GetExchange("/tasks/{tg-id}")
    public ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable("tg-id") Long tgId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) OffsetDateTime at_ended
    ) {
        return taskClient.getAll(tgId, status, at_ended);
    }

    @DeleteExchange("/tasks/{tg-id}/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("tg-id") Long tgId,
            @PathVariable("id") Long id
    ){
        taskClient.deleteTask(tgId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(@RequestParam("query") String query) {
        List<SearchItem> items = new ArrayList<>();

        try {
            ResponseEntity<List<ContactResponse>> contacts = contactClient.search(query);
            if (contacts.getBody() != null) {
                contacts.getBody().forEach(c ->
                        items.add(new SearchItem(SearchType.CONTACT, c.id().toString(), c.name()))
                );
            }
        } catch (Exception e) {
            log.warn("Ошибка поиска контактов: {}", e.getMessage());
        }

        try {
            ResponseEntity<List<DocumentResponse>> documents = documentClient.search(query);
            if (documents.getBody() != null) {
                documents.getBody().forEach(d ->
                        items.add(new SearchItem(SearchType.DOCUMENT, d.id().toString(), d.content()))
                );
            }
        } catch (Exception e) {
            log.warn("Ошибка поиска документов: {}", e.getMessage());
        }

        try {
            ResponseEntity<List<TaskResponse>> tasks = taskClient.search(query);
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

