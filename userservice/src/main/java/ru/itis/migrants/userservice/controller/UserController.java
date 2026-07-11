package ru.itis.migrants.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.migrants.userservice.api.UserApi;
import ru.itis.migrants.userservice.dto.request.UserRequest;
import ru.itis.migrants.userservice.dto.response.UserResponse;
import ru.itis.migrants.userservice.service.inter.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public UserResponse create(UserRequest request) {
        return userService.create(request);
    }

    @Override
    public UserResponse getById(Long tgId) {
        return userService.getById(tgId);
    }

    @Override
    public UserResponse deleteById(Long tgId) {
        return userService.deleteById(tgId);
    }
}