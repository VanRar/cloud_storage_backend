package ru.netology.cloud_storage_backend.dto;

import lombok.Data;

@Data
public class AuthenticationDTO {
    private String login;
    private String password;
}
