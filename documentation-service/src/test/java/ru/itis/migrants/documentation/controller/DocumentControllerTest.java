package ru.itis.migrants.documentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.itis.migrants.documentation.dto.CreateDocumentRequest;
import ru.itis.migrants.documentation.dto.DocumentResponse;
import ru.itis.migrants.documentation.exception.DocumentNotFoundException;
import ru.itis.migrants.documentation.service.DocumentService;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    private static final Long OWNER_ID = 20L;
    private static final UUID DOCUMENT_ID = UUID.fromString("1c07fa64-d1c7-4e3c-b378-03273c2ef636");
    private static final OffsetDateTime CREATED_AT = OffsetDateTime.parse("2026-07-10T10:26:30Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DocumentService documentService;

    @Test
    void postValidDocumentReturnsCreated() throws Exception {
        when(documentService.create(eq(OWNER_ID), any(CreateDocumentRequest.class))).thenReturn(response());

        mockMvc.perform(post("/documents/{tgId}", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Mi nota importante\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/documents/20/%s".formatted(DOCUMENT_ID)))
                .andExpect(jsonPath("$.id").value(DOCUMENT_ID.toString()))
                .andExpect(jsonPath("$.ownerId").value(OWNER_ID))
                .andExpect(jsonPath("$.content").value("Mi nota importante"));
    }

    @Test
    void postBlankContentReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/documents/{tgId}", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"   \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Content must not be blank"));
    }

    @Test
    void getDocumentsReturnsList() throws Exception {
        when(documentService.findAllByOwnerId(OWNER_ID)).thenReturn(List.of(response()));

        mockMvc.perform(get("/documents/{tgId}", OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(DOCUMENT_ID.toString()))
                .andExpect(jsonPath("$[0].ownerId").value(OWNER_ID));
    }

    @Test
    void searchWithValidQueryReturnsOk() throws Exception {
        when(documentService.search(OWNER_ID, "importante")).thenReturn(List.of(response()));

        mockMvc.perform(get("/documents/{tgId}/search", OWNER_ID).param("query", "importante"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Mi nota importante"));
    }

    @Test
    void searchWithBlankQueryReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/documents/{tgId}/search", OWNER_ID).param("query", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Query must not be blank"));
    }

    @Test
    void deleteExistingDocumentReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/documents/{tgId}/{documentId}", OWNER_ID, DOCUMENT_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMissingDocumentReturnsNotFound() throws Exception {
        doThrow(new DocumentNotFoundException(DOCUMENT_ID, OWNER_ID))
                .when(documentService)
                .delete(OWNER_ID, DOCUMENT_ID);

        mockMvc.perform(delete("/documents/{tgId}/{documentId}", OWNER_ID, DOCUMENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    private DocumentResponse response() {
        return new DocumentResponse(DOCUMENT_ID, OWNER_ID, "Mi nota importante", CREATED_AT);
    }
}
