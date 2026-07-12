package ru.itis.migrants.contactservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.migrants.contactservice.model.Contact;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactJpaRepository extends JpaRepository<Contact, UUID> {
    List<Contact> findContactByOwnerId(Long tgId);

    @Query(value = "SELECT * FROM contact c " +
            "WHERE c.owner_id = :ownerId " +
            "AND c.search_vector @@ to_tsquery('simple', :query)",
            nativeQuery = true)
    List<Contact> searchByOwnerIdAndQuery(@Param("ownerId") Long ownerId,
                                          @Param("query") String query);
}
