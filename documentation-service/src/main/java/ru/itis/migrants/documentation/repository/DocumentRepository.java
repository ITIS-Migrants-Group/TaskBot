package ru.itis.migrants.documentation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.migrants.documentation.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findAllByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    List<Document> findAllByOwnerIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(Long ownerId, String query);

    Optional<Document> findByIdAndOwnerId(UUID id, Long ownerId);
}
