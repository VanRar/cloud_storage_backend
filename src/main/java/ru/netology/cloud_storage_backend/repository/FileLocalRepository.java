package ru.netology.cloud_storage_backend.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileLocalRepository {
  boolean saveFile(MultipartFile file, String fileName, String path) throws IOException;

  boolean deleteFile(String fileName, String path);

  boolean renameFile(String fileName, String path, String newName);
}
