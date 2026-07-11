package ru.itis.migrants.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.migrants.starter.logging.annotation.LogAround;
import ru.itis.migrants.userservice.dto.request.UserRequest;
import ru.itis.migrants.userservice.dto.response.UserResponse;
import ru.itis.migrants.userservice.exception.client.UserAlreadyExistsException;
import ru.itis.migrants.userservice.exception.client.UserNotFoundException;
import ru.itis.migrants.userservice.jpa.entity.User;
import ru.itis.migrants.userservice.repository.UserRepository;
import ru.itis.migrants.userservice.service.inter.UserService;
import ru.itis.migrants.userservice.util.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse create(UserRequest request) {
        User user = userMapper.toEntity(request);
        if(userRepository.existsById(request.tgId())) {
            throw UserAlreadyExistsException.byId(request.tgId());
        }
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getById(Long tgId) {
        return userMapper.toResponse(userRepository.findById(tgId)
                .orElseThrow(() -> UserNotFoundException.byId(tgId)));
    }

    @Override
    @Transactional
    public UserResponse deleteById(Long tgId) {
        User user = userRepository.findById(tgId)
                .orElseThrow(() -> UserNotFoundException.byId(tgId));
        userRepository.delete(user);
        return userMapper.toResponse(user);
    }
}