package ru.itis.migrants.documentation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.migrants.documentation.dto.CreateDocumentRequest;
import ru.itis.migrants.documentation.dto.DocumentResponse;
import ru.itis.migrants.documentation.service.DocumentService;

@Validated
@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/{tgId}")
    public ResponseEntity<DocumentResponse> create(
            @PathVariable @Positive(message = "Telegram id must be positive") Long tgId,
            @Valid @RequestBody CreateDocumentRequest request
    ) {
        DocumentResponse response = documentService.create(tgId, request);
        return ResponseEntity
                .created(URI.create("/documents/%d/%s".formatted(tgId, response.id())))
                .body(response);
    }

    @GetMapping("/{tgId}")
    public ResponseEntity<List<DocumentResponse>> findAllByOwnerId(
            @PathVariable @Positive(message = "Telegram id must be positive") Long tgId
    ) {
        return ResponseEntity.ok(documentService.findAllByOwnerId(tgId));
    }

    @GetMapping("/{tgId}/search")
    public ResponseEntity<List<DocumentResponse>> search(
            @PathVariable @Positive(message = "Telegram id must be positive") Long tgId,
            @RequestParam @NotBlank(message = "Query must not be blank") String query
    ) {
        return ResponseEntity.ok(documentService.search(tgId, query));
    }

    @DeleteMapping("/{tgId}/{documentId}")
    public ResponseEntity<DocumentResponse> delete(
            @PathVariable @Positive(message = "Telegram id must be positive") Long tgId,
            @PathVariable UUID documentId
    ) {
        return ResponseEntity.ok(documentService.delete(tgId, documentId));
    }
}
