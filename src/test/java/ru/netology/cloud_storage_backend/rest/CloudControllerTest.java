package ru.netology.cloud_storage_backend.rest;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.netology.cloud_storage_backend.dto.FileDTO;
import ru.netology.cloud_storage_backend.security.jwt.JwtTokenProvider;
import ru.netology.cloud_storage_backend.service.CloudService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CloudController.class)
public class CloudControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CloudService cloudService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Value("classpath:files\\test.txt")
    static Resource resourceFile;

    private static final String token = "Bearer token";
    private static final String fileName = "test.txt";

    @Test
    @SneakyThrows
    public void whenGetList_thenStatusOk() {
        var list = List.of(
                FileDTO.builder().filename("name").size(100L).build()
        );
        when(cloudService.getFiles(token, 1)).thenReturn(list);
        mockMvc.perform(get("/list").header("auth-token", token).param("limit", "1"))
                .andExpect(status().isOk());
    }
}