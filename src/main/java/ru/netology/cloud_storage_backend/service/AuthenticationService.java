package ru.netology.cloud_storage_backend.service;


import ru.netology.cloud_storage_backend.dto.AuthenticationDTO;

public interface AuthenticationService {
    String getToken(AuthenticationDTO authenticationDTO);

    void removeToken(String token);
}
