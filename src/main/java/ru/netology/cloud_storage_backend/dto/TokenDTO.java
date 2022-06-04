package ru.netology.cloud_storage_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDTO {

    @JsonProperty("auth-token")
    private String value;
}
