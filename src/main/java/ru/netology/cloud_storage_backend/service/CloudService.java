package ru.netology.cloud_storage_backend.service;

import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage_backend.dto.FileDTO;


import java.io.File;
import java.util.List;

public interface CloudService {
    List<FileDTO> getFiles(String token, int limit);

    File getFile(String token, String fileName);

    void renameFile(String token, String fileName, String newName);

    void uploadFile(String token, MultipartFile file, String fileName);

    void deleteFile(String token, String fileName);
}
