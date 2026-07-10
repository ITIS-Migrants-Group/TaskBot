package ru.itis.migrants.documentation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.migrants.documentation.dto.CreateDocumentRequest;
import ru.itis.migrants.documentation.dto.DocumentResponse;
import ru.itis.migrants.documentation.entity.Document;
import ru.itis.migrants.documentation.exception.DocumentNotFoundException;
import ru.itis.migrants.documentation.mapper.DocumentMapper;
import ru.itis.migrants.documentation.repository.DocumentRepository;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    private static final Long OWNER_ID = 20L;
    private static final UUID DOCUMENT_ID = UUID.fromString("1c07fa64-d1c7-4e3c-b378-03273c2ef636");
    private static final OffsetDateTime CREATED_AT = OffsetDateTime.parse("2026-07-10T10:26:30Z");

    @Mock
    private DocumentRepository documentRepository;

    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        documentService = new DocumentService(documentRepository, new DocumentMapper());
    }

    @Test
    void createTrimsAndSavesDocument() {
        when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> {
            Document document = invocation.getArgument(0);
            document.setId(DOCUMENT_ID);
            document.setCreatedAt(CREATED_AT);
            return document;
        });

        DocumentResponse response = documentService.create(OWNER_ID, new CreateDocumentRequest("  important note  "));

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository).save(documentCaptor.capture());
        assertThat(documentCaptor.getValue().getOwnerId()).isEqualTo(OWNER_ID);
        assertThat(documentCaptor.getValue().getContent()).isEqualTo("important note");
        assertThat(response.id()).isEqualTo(DOCUMENT_ID);
        assertThat(response.content()).isEqualTo("important note");
    }

    @Test
    void findAllByOwnerIdReturnsDocumentsOrderedByRepository() {
        Document first = document(DOCUMENT_ID, OWNER_ID, "newer", CREATED_AT.plusHours(1));
        Document second = document(UUID.randomUUID(), OWNER_ID, "older", CREATED_AT);
        when(documentRepository.findAllByOwnerIdOrderByCreatedAtDesc(OWNER_ID)).thenReturn(List.of(first, second));

        List<DocumentResponse> responses = documentService.findAllByOwnerId(OWNER_ID);

        assertThat(responses).extracting(DocumentResponse::content).containsExactly("newer", "older");
        verify(documentRepository).findAllByOwnerIdOrderByCreatedAtDesc(OWNER_ID);
    }

    @Test
    void searchUsesCaseInsensitiveOwnerScopedRepositoryMethod() {
        Document document = document(DOCUMENT_ID, OWNER_ID, "Mi Nota Importante", CREATED_AT);
        when(documentRepository.findAllByOwnerIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(
                OWNER_ID,
                "importante"
        )).thenReturn(List.of(document));

        List<DocumentResponse> responses = documentService.search(OWNER_ID, " importante ");

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().content()).isEqualTo("Mi Nota Importante");
        verify(documentRepository)
                .findAllByOwnerIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(OWNER_ID, "importante");
    }

    @Test
    void searchWithoutResultsReturnsEmptyList() {
        when(documentRepository.findAllByOwnerIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(
                OWNER_ID,
                "missing"
        )).thenReturn(List.of());

        List<DocumentResponse> responses = documentService.search(OWNER_ID, "missing");

        assertThat(responses).isEmpty();
    }

    @Test
    void findAllDoesNotReturnDocumentsFromAnotherOwner() {
        Document ownDocument = document(DOCUMENT_ID, OWNER_ID, "own", CREATED_AT);
        Document otherDocument = document(UUID.randomUUID(), 99L, "foreign", CREATED_AT);
        when(documentRepository.findAllByOwnerIdOrderByCreatedAtDesc(OWNER_ID))
                .thenReturn(List.of(ownDocument, otherDocument));

        List<DocumentResponse> responses = documentService.findAllByOwnerId(OWNER_ID);

        assertThat(responses).extracting(DocumentResponse::ownerId).containsExactly(OWNER_ID);
    }

    @Test
    void deleteRemovesDocumentOwnedByUser() {
        Document document = document(DOCUMENT_ID, OWNER_ID, "own", CREATED_AT);
        when(documentRepository.findByIdAndOwnerId(DOCUMENT_ID, OWNER_ID)).thenReturn(Optional.of(document));

        documentService.delete(OWNER_ID, DOCUMENT_ID);

        verify(documentRepository).delete(document);
    }

    @Test
    void deleteThrowsWhenDocumentDoesNotExist() {
        when(documentRepository.findByIdAndOwnerId(DOCUMENT_ID, OWNER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.delete(OWNER_ID, DOCUMENT_ID))
                .isInstanceOf(DocumentNotFoundException.class);
        verify(documentRepository, never()).delete(any());
    }

    @Test
    void deleteDoesNotRemoveDocumentFromAnotherOwner() {
        Long anotherOwner = 99L;
        when(documentRepository.findByIdAndOwnerId(DOCUMENT_ID, anotherOwner)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.delete(anotherOwner, DOCUMENT_ID))
                .isInstanceOf(DocumentNotFoundException.class);
        verify(documentRepository, never()).delete(any());
    }

    private Document document(UUID id, Long ownerId, String content, OffsetDateTime createdAt) {
        return Document.builder()
                .id(id)
                .ownerId(ownerId)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}
