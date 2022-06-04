package ru.netology.cloud_storage_backend.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.netology.cloud_storage_backend.exception.CloudServiceNotFoundException;
import ru.netology.cloud_storage_backend.model.user.User;
import ru.netology.cloud_storage_backend.repository.UserRepository;
import ru.netology.cloud_storage_backend.service.UserService;


import static java.lang.String.format;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final static String USER_NOT_FOUND_LOGIN = "User with login=[%s] not found";
    private final static String USER_NOT_FOUND_ID = "User with id=[%s] not found";

    private final UserRepository userRepository;

    @Override
    public User findByEmail(String login) {
        var user = userRepository.findByEmail(login)
                .orElseThrow(() -> new CloudServiceNotFoundException(format(USER_NOT_FOUND_LOGIN, login)));
        log.info("User: {} found by login: {}", user, login);
        return user;
    }

    @Override
    public User findById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new CloudServiceNotFoundException(format(USER_NOT_FOUND_ID, id)));
        log.info("User: {} found by id: {}", user, id);
        return user;
    }

}
