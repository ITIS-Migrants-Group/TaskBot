package ru.itis.migrants.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLIntervalSecondJdbcType;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false, length = 1023)
    private String title;

    private UUID taskId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false)
    private OffsetDateTime notifyAt;

    @JdbcType(PostgreSQLIntervalSecondJdbcType.class)
    @Column(columnDefinition = "interval")
    private Duration period;

    @Column(nullable = false)
    private Boolean isActive;

}
