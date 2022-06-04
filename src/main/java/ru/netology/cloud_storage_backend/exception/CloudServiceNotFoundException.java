package ru.netology.cloud_storage_backend.exception;

public class CloudServiceNotFoundException extends RuntimeException{
    public CloudServiceNotFoundException(String message) {
        super(message);
    }
}
