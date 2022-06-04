package ru.netology.cloud_storage_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDTO {
    private String filename;
    private long size;
}
