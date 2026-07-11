package ru.itis.migrants.notificationservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.migrants.notificationservice.dto.NotificationResponse;
import ru.itis.migrants.notificationservice.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "taskId", expression = "java(java.util.Optional.ofNullable(entity.getTaskId()))")
    @Mapping(target = "period", expression = "java(java.util.Optional.ofNullable(entity.getPeriod()))")
    NotificationResponse fromEntity(Notification entity);

    @Mapping(target = "taskId", expression = "java(response.taskId().orElse(null))")
    @Mapping(target = "period", expression = "java(response.period().orElse(null))")
    Notification toEntity(NotificationResponse response);
}