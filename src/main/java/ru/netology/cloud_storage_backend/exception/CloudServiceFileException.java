package ru.netology.cloud_storage_backend.exception;

public class CloudServiceFileException extends RuntimeException{
  public CloudServiceFileException(String message) {
    super(message);
  }
}
