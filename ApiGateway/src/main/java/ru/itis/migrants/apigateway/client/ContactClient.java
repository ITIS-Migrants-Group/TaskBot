package ru.itis.migrants.apigateway.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.itis.migrants.apigateway.dto.request.CreateContactRequest;
import ru.itis.migrants.apigateway.dto.response.ContactResponse;

import java.util.List;

@Component
@HttpExchange("/contacts")
public interface ContactClient {

    @PostExchange("/{tg-id}")
    ResponseEntity<ContactResponse> create(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateContactRequest request
    );

    @GetExchange("/{tg-id}/search")
    ResponseEntity<List<ContactResponse>> search(
            @PathVariable("tg-id") Long tgId,
            @RequestParam String query);

}