package ru.itis.migrants.documentation.exception;

import java.util.UUID;

public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(UUID documentId, Long ownerId) {
        super("Document '%s' was not found for owner '%d'".formatted(documentId, ownerId));
    }
}
