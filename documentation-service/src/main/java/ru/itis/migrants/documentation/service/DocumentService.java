package ru.itis.migrants.documentation.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.migrants.documentation.dto.CreateDocumentRequest;
import ru.itis.migrants.documentation.dto.DocumentResponse;
import ru.itis.migrants.documentation.entity.Document;
import ru.itis.migrants.documentation.exception.DocumentNotFoundException;
import ru.itis.migrants.documentation.exception.InvalidSearchQueryException;
import ru.itis.migrants.documentation.mapper.DocumentMapper;
import ru.itis.migrants.documentation.repository.DocumentRepository;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    @Transactional
    public DocumentResponse create(Long ownerId, CreateDocumentRequest request) {
        String content = request.content().trim();
        Document document = Document.builder()
                .ownerId(ownerId)
                .content(content)
                .build();
        return documentMapper.toResponse(documentRepository.save(document));
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> findAllByOwnerId(Long ownerId) {
        return documentRepository.findAllByOwnerIdOrderByCreatedAtDesc(ownerId).stream()
                .filter(document -> ownerId.equals(document.getOwnerId()))
                .map(documentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> search(Long ownerId, String query) {
        String trimmedQuery = query == null ? "" : query.trim();
        if (trimmedQuery.isBlank()) {
            throw new InvalidSearchQueryException("Query must not be blank");
        }

        return documentRepository
                .findAllByOwnerIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(ownerId, trimmedQuery)
                .stream()
                .filter(document -> ownerId.equals(document.getOwnerId()))
                .map(documentMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long ownerId, UUID documentId) {
        Document document = documentRepository.findByIdAndOwnerId(documentId, ownerId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId, ownerId));
        documentRepository.delete(document);
    }
}
