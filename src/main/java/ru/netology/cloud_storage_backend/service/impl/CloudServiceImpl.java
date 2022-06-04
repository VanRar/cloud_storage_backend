package ru.netology.cloud_storage_backend.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage_backend.model.file.File;
import ru.netology.cloud_storage_backend.model.file.Status;
import ru.netology.cloud_storage_backend.repository.FileLocalRepository;
import ru.netology.cloud_storage_backend.repository.FileRepository;
import ru.netology.cloud_storage_backend.security.jwt.JwtTokenProvider;
import ru.netology.cloud_storage_backend.service.CloudService;
import ru.netology.cloud_storage_backend.dto.FileDTO;



import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.netology.cloud_storage_backend.security.jwt.JwtTokenProvider.BEARER_LENGTH;


@Service
@Slf4j
public class CloudServiceImpl implements CloudService {

  @Value("${data.files.path}")
  private String path;
  private final static String FULL_PATH = "%s\\%s\\";

  private final FileRepository repository;
  private final JwtTokenProvider tokenProvider;
  private final FileLocalRepository localRepository;

  @Autowired
  public CloudServiceImpl(FileRepository repository, JwtTokenProvider tokenProvider, FileLocalRepository localRepository) {
    this.repository = repository;
    this.tokenProvider = tokenProvider;
    this.localRepository = localRepository;
  }

  @PostConstruct
  private void init() {
    var checkPath = Paths.get(path);
    if (!Files.exists(checkPath)) {
      var file = new java.io.File(path);
      file.mkdir();
      log.info("[{}] path was created.", checkPath);
    }
  }

  @Override
  public List<FileDTO> getFiles(String token, int limit) {
    log.info("Getting files with token =[{}] and limit=[{}]", token, limit);
    var username = getUsername(token);
    var files = repository.findByUsernameAndStatus(username, Status.ACTIVE);
    return files.stream()
        .limit(limit)
        .map(this::convertFromFile)
        .collect(Collectors.toList());
  }

  @Override
  public java.io.File getFile(String token, String filename) {
    log.info("Getting file with token=[{}] and filename=[{}]", token, filename);
    var username = getUsername(token);
    var fullPath = repository.findByUsernameAndNameAndStatus(username, filename, Status.ACTIVE)
        .orElseThrow(() -> new CloudServiceNotFoundException(format("File with name=[%s] not found.", filename)))
        .getPath();
    return new java.io.File(fullPath + "//" + filename);
  }

  @Override
  public void renameFile(String token, String filename, String newName) {
    log.info("Renaming file with token=[{}] and filename=[{}] and new name=[{}]", token, filename, newName);
    var username = getUsername(token);
    var file = repository.findByUsernameAndNameAndStatus(username, filename, Status.ACTIVE)
        .orElseThrow(() -> new CloudServiceNotFoundException(format("File with name=[%s] not found.", filename)));
    if (localRepository.renameFile(filename, file.getPath(), newName)) {
      file.setName(newName);
      repository.save(file);
    } else {
      throw new CloudServiceFileException("Something went wrong with file renaming. Please contact support");
    }
  }

  @Override
  public void uploadFile(String token, MultipartFile multipartFile, String fileName) {
    log.info("Uploading file with token=[{}] and filename=[{}]", token, fileName);
    var username = getUsername(token);
    var fullPath = format(FULL_PATH, path, username);
    try {
      if (localRepository.saveFile(multipartFile, fileName, fullPath)) {
        var now = new Date(System.currentTimeMillis());
        var file = File.builder()
            .name(fileName)
            .path(fullPath)
            .username(username)
            .size(multipartFile.getBytes().length)
            .created(now)
            .updated(now)
            .status(Status.ACTIVE)
            .build();
        repository.save(file);
      } else {
        throw new CloudServiceFileException("Something went wrong with file uploading. Please contact support");
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw new CloudServiceFileException("Something went wrong with file uploading. Please contact support");
    }
  }

  @Override
  public void deleteFile(String token, String filename) {
    log.info("Deleting file with token=[{}] and filename=[{}]", token, filename);
    var username = getUsername(token);
    var fullPath = format(FULL_PATH, path, username);
    if (localRepository.deleteFile(filename, fullPath)) {
      var file = repository.findByUsernameAndNameAndStatus(username, filename, Status.ACTIVE)
          .orElseThrow(() -> new CloudServiceNotFoundException(format("File with name=[%s] not found.", filename)));
      file.setStatus(Status.DELETED);
      repository.save(file);
    }
  }

  private String getUsername(String token) {
    return tokenProvider.getUsername(token.substring(BEARER_LENGTH));
  }

  private FileDTO convertFromFile(File file) {
    return FileDTO.builder()
        .filename(file.getName())
        .size(file.getSize())
        .build();
  }
}
