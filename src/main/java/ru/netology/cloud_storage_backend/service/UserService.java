package ru.netology.cloud_storage_backend.service;


import ru.netology.cloud_storage_backend.model.user.User;

public interface UserService {
    User findByEmail(String login);

    User findById(Long id);
}
