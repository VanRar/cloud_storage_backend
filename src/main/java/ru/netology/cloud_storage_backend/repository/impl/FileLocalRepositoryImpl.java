package ru.netology.cloud_storage_backend.repository.impl;

import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage_backend.repository.FileLocalRepository;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Repository
public class FileLocalRepositoryImpl implements FileLocalRepository {

  @Value("${data.files.remove.flag}")
  private boolean remove;

  @Override
  public boolean saveFile(MultipartFile multipartFile, String fileName, String path) throws IOException {
    var file = new File(path + fileName);
    if (file.exists() || multipartFile.isEmpty()) return false;

    var checkPath = Paths.get(path);
    if (!Files.exists(checkPath)) {
      var dir = new File(path);
      dir.mkdir();
    }

    byte[] bytes = multipartFile.getBytes();
    @Cleanup BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
    stream.write(bytes);
    return true;
  }

  @Override
  public boolean deleteFile(String fileName, String path) {
    if (remove) {
      var file = new File(path + fileName);
      if (!file.exists()) return true;
      return file.delete();
    }
    return true;
  }

  @Override
  public boolean renameFile(String fileName, String path, String newName) {
    var file = new File(path + fileName);
    if (!file.exists()) return false;
    return file.renameTo(new File(path + "//" + newName));
  }
}
