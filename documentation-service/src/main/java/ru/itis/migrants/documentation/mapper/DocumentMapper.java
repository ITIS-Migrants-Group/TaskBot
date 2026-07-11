package ru.itis.migrants.documentation.mapper;

import org.springframework.stereotype.Component;
import ru.itis.migrants.documentation.dto.DocumentResponse;
import ru.itis.migrants.documentation.entity.Document;

@Component
public class DocumentMapper {

    public DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getOwnerId(),
                document.getContent(),
                document.getCreatedAt()
        );
    }
}
