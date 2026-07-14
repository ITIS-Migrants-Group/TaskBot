package ru.itis.migrants.notificationservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.migrants.notificationservice.dto.NotificationResponse;
import ru.itis.migrants.notificationservice.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse fromEntity(Notification entity);

    Notification toEntity(NotificationResponse response);
}