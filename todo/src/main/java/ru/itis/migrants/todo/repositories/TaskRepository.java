package ru.itis.migrants.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.migrants.todo.models.Task;
import ru.itis.migrants.todo.models.TaskStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByTgChatIdAndStatusAndEndedAt(Long tgChatId, TaskStatus status, OffsetDateTime endedAt);

    List<Task> findAllByTgChatId(Long tgChatId);

    List<Task> findAllByTgChatIdAndStatus(Long tgChatId, TaskStatus status);

    List<Task> findAllByTgChatIdAndEndedAt(Long tgChatId, OffsetDateTime endedAt);

    @Query("SELECT t FROM Task t WHERE t.tgChatId = :tgId AND " +
            "(LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Task> findByKeyword(@Param("tgId") Long tgId, @Param("query") String query);
}
