package ru.itis.migrants.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.migrants.notificationservice.model.Notification;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationJpaRepository extends JpaRepository<Notification, UUID> {

    @Query("""
    FROM Notification n
    WHERE n.isActive = TRUE
    AND n.notify_at < :time
    """)
    List<Notification> findForScheduler(@Param("time") OffsetDateTime time);
}
