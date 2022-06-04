package ru.netology.cloud_storage_backend.exception.handling;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.cloud_storage_backend.dto.ErrorDTO;
import ru.netology.cloud_storage_backend.exception.CloudServiceFileException;


@RestControllerAdvice
@Slf4j
public class CloudServiceGlobalExceptionHandler {

    @Hidden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorDTO badCredentialsExceptionHandler(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        return ErrorDTO.builder()
                .message(e.getMessage())
                .build();
    }

    @Hidden
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CloudServiceFileException.class)
    public ErrorDTO cloudServiceFileException(CloudServiceFileException e) {
        log.error(e.getMessage(), e);
        return ErrorDTO.builder()
            .message(e.getMessage())
            .build();
    }

    @Hidden
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDTO exception(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorDTO.builder()
            .message(e.getMessage())
            .build();
    }
}
