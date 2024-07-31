package com.pavel.dinit.project.services;

import com.pavel.dinit.project.repo.UrlRepo;
import com.pavel.dinit.project.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {


    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UrlService urlService;

    @Test
    void checkUrlHealth1_Successful() {
        // Arrange
        when(restTemplate.getForEntity("http://youtube.com", String.class))
                .thenReturn((new ResponseEntity<>("Healthy", HttpStatus.OK)));

        // Act
        boolean result = urlService.checkUrlHealth1("http://youtube.com");


        // Assert
        assertTrue(result);
    }

    @Test
    void checkUrlHealth1_ThrowingConflict() {
        // Arrange
        when(restTemplate.getForEntity("http://invalid-url.com", String.class)).thenThrow(new RuntimeException());

        // Act
        boolean result = urlService.checkUrlHealth1("http://invalid-url.com");

        // Assert
        assertFalse(result);

    }
}
