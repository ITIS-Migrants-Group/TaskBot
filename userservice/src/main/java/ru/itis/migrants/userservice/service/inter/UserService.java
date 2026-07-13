package ru.itis.migrants.userservice.service.inter;

import ru.itis.migrants.userservice.dto.request.UserRequest;
import ru.itis.migrants.userservice.dto.response.UserResponse;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse getById(Long tgId);

    UserResponse deleteById(Long tgId);
}
