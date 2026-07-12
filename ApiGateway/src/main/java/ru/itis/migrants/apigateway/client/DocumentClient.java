package ru.itis.migrants.apigateway.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.itis.migrants.apigateway.dto.request.CreateDocumentRequest;
import ru.itis.migrants.apigateway.dto.response.DocumentResponse;

import java.util.List;

@Component
@HttpExchange("/documents")
public interface DocumentClient {

    @PostExchange("/{tg-id}")
    ResponseEntity<DocumentResponse> create(
            @PathVariable("tg-id") Long tgId,
            @RequestBody CreateDocumentRequest request
    );

    @GetExchange("/search")
    ResponseEntity<List<DocumentResponse>> search(@RequestParam String query);
}
