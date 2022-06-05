package ru.netology.cloud_storage_backend.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import ru.netology.cloud_storage_backend.exception.CloudServiceFileException;
import ru.netology.cloud_storage_backend.exception.CloudServiceNotFoundException;
import ru.netology.cloud_storage_backend.model.file.File;
import ru.netology.cloud_storage_backend.model.file.Status;
import ru.netology.cloud_storage_backend.repository.FileLocalRepository;
import ru.netology.cloud_storage_backend.repository.FileRepository;
import ru.netology.cloud_storage_backend.security.jwt.JwtTokenProvider;
import ru.netology.cloud_storage_backend.service.impl.CloudServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.cloud_storage_backend.security.jwt.JwtTokenProvider.BEARER_LENGTH;

@RunWith(SpringRunner.class)
class CloudServiceImplTest {

    private static CloudService cloudService;

    private static String token = "token abcabc";
    private static String user = "user";
    private static Status active = Status.ACTIVE;
    private static Date now = new Date(System.currentTimeMillis());
    private static File file = new File(1L, now, now, active, "test", user, "path", 100L);
    private static List<File> files = List.of(
            new File(1L, now, now, active, "name1", user, "path", 100L),
            new File(2L, now, now, active, "name2", user, "path", 150L)
    );

    @BeforeAll
    public static void init() {
        var fileRepository = Mockito.mock(FileRepository.class);
        var jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        var fileLocalRepository = Mockito.mock(FileLocalRepository.class);
        Mockito.when(fileRepository.findByUsernameAndStatus(user, active))
                .thenReturn(files);
        Mockito.when(jwtTokenProvider.getUsername(token.substring(BEARER_LENGTH))).thenReturn(user);
        Mockito.when(fileRepository.findByUsernameAndNameAndStatus(user, "test", active))
                .thenReturn(Optional.of(file));
        cloudService = new CloudServiceImpl(fileRepository, jwtTokenProvider, fileLocalRepository);
    }

    @Test
    void whenGetFilesThenReturnsList() {
        var filesDTO = cloudService.getFiles(token, 10);
        assertEquals(filesDTO.size(), 2);
        assertEquals("name1", filesDTO.get(0).getFilename());
        assertEquals("name2", filesDTO.get(1).getFilename());
    }

    @Test
    void whenGetFileThenReturnFileWithRightProp() {
        var javaFile = cloudService.getFile(token, "test");
        assertEquals("test", javaFile.getName());
        assertEquals("path\\test", javaFile.getPath());
    }

    @Test
    void whenRenameFileThenExceptionThrows() {
        Throwable thrown = assertThrows(CloudServiceNotFoundException.class, () -> cloudService.renameFile(token, "", ""));
        assertNotNull(thrown.getMessage());
    }

    @Test
    void whenUploadFileThenExceptionThrows() {
        Throwable thrown = assertThrows(CloudServiceFileException.class, () -> cloudService.uploadFile(token, null, ""));
        assertNotNull(thrown.getMessage());
    }
}