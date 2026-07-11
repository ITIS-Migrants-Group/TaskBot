package ru.itis.migrants.userservice.util.mapper;


import org.mapstruct.Mapper;
import ru.itis.migrants.userservice.dto.request.UserRequest;
import ru.itis.migrants.userservice.dto.response.UserResponse;
import ru.itis.migrants.userservice.jpa.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    User toEntity(UserRequest request);
}
