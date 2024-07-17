package com.pavel.dinit.project.services;

import com.pavel.dinit.project.dtos.UrlCreationDto;
import com.pavel.dinit.project.dtos.UrlReadingDto;
import com.pavel.dinit.project.exceptions.badrequest.ApiBadRequest;
import com.pavel.dinit.project.exceptions.conflict.Conflict;
import com.pavel.dinit.project.exceptions.notfound.ResourceNotFound;
import com.pavel.dinit.project.models.Url;
import com.pavel.dinit.project.models.User;
import com.pavel.dinit.project.repo.UrlRepo;
import com.pavel.dinit.project.repo.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepo urlRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UrlService urlService;



    @Test
    void getAllUrls_Successful() {
        // Arrange
        User user1 = new User(1L, "Pavel", "password", "USER", null);
        User user2 = new User(2L, "NotPavel", "password", "ADMIN,USER", null);

        Url url1 = new Url(1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01", user1);
        Url url2 = new Url(2L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02", user2);

        List<Url> listOfUrls = List.of(url1, url2);

        when(urlRepo.findAll()).thenReturn(listOfUrls);

        UrlReadingDto urlReadDto1 = UrlReadingDto.readingDtoFromUrl(listOfUrls.get(0));
        UrlReadingDto urlReadDto2 = UrlReadingDto.readingDtoFromUrl(listOfUrls.get(1));

        // Act
        List<UrlReadingDto> allUrls = urlService.getAllUrls();

        // Assert
        Assertions.assertEquals(2, listOfUrls.size());
        assertEquals(urlReadDto1, allUrls.get(0));
        assertEquals(urlReadDto2, allUrls.get(1));
    }

    @Test
    void getAllUrls_ThrowingResourceNotFound() {

        // Mock empty list
        List<Url> listOfUrls = new ArrayList<>();

        // Mock repo
        when(urlRepo.findAll()).thenReturn(listOfUrls);

        // Assert
        assertThrows(ResourceNotFound.class, () -> urlService.getAllUrls());


        // Verify repo method was called
        verify(urlRepo, times(1)).findAll();
    }


    @Test
    void getUrlById_Successful() {
        // Arrange
        Long urlId1 = 1L;
        Long urlId2 = 2L;
        User user1 = new User(1L, "Pavel", "password", "USER", null);
        User user2 = new User(2L, "NotPavel", "password", "ADMIN,USER", null);

        Url url1 = new Url(1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01", user1);
        Url url2 = new Url(2L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02", user2);

        // Mock the behavior of urlService.getUrlById
        when(urlRepo.findByUrlId(urlId1)).thenReturn(Optional.of(url1));
        when(urlRepo.findByUrlId(urlId2)).thenReturn(Optional.of(url2));

        // Act
        UrlReadingDto urlReadDto1 = urlService.getUrlById(urlId1);
        UrlReadingDto urlReadDto2 = urlService.getUrlById(urlId2);

        // Assert
        assertEquals(url1.getUrlId(), urlReadDto1.getUrlId());
        assertEquals(url2.getUrlId(), urlReadDto2.getUrlId());

        // Compare the user IDs
        assertEquals(url1.getAddedByUserId().getId(), urlReadDto1.getAddedByUserId());
        assertEquals(url2.getAddedByUserId().getId(), urlReadDto2.getAddedByUserId());

        assertEquals(url1.getUrlName(), urlReadDto1.getUrlName());
        assertEquals(url2.getUrlName(), urlReadDto2.getUrlName());

        assertEquals(url1.getFullUrl(), urlReadDto1.getFullUrl());
        assertEquals(url2.getFullUrl(), urlReadDto2.getFullUrl());

        assertEquals(url1.getUrlHealth(), urlReadDto1.getUrlHealth());
        assertEquals(url2.getUrlHealth(), urlReadDto2.getUrlHealth());

        assertEquals(url1.getLastChecked(), urlReadDto1.getLastChecked());
        assertEquals(url2.getLastChecked(), urlReadDto2.getLastChecked());
    }



    @Test
    void getUrlById_ThrowingResourceNotFound() {
        // Arrange
        Long urlId = 3L;

        // Mock repo
        when(urlRepo.findByUrlId(urlId)).thenReturn(Optional.empty());


        // Act
        ResourceNotFound notFound = assertThrows(ResourceNotFound.class, () -> urlService.getUrlById(urlId));

        // Assert
        assertEquals("URL with name " + urlId + " not found.", notFound.getMessage());
        verify(urlRepo, times(1)).findByUrlId(urlId);
    }


    @Test
    void getAllUrlNames_Successful() {
        // Arrange
        User user1 = new User(1L, "Pavel", "password", "USER", null);
        User user2 = new User(2L, "NotPavel", "password", "ADMIN,USER", null);

        Url url1 = new Url(1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01", user1);
        Url url2 = new Url(2L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02", user2);

        when(urlRepo.findAll()).thenReturn(List.of(url1, url2));


        // Act
        List<Object> result = urlService.getAllUrlNames();


        // Assert
        assertEquals(2, result.size());
        assertEquals("YouTube", result.get(0));
        assertEquals("GitHub", result.get(1));

    }


    @Test
    void getAllUrlNames_ThrowingResourceNotFound() {
        // Arrange

        // Act and Assert
        Assertions.assertThrows(ResourceNotFound.class, () -> urlService.getAllUrlNames());
    }

    @Test
    void deleteUrlById_Successful() {
        // Arrange
        Long urlId1 = 1L;
        Long urlId2 = 2L;


        when(urlRepo.existsById(urlId1)).thenReturn(true);
        when(urlRepo.existsById(urlId2)).thenReturn(true);

        // Act
        String result1 = urlService.deleteUrlById(urlId1);
        String result2 = urlService.deleteUrlById(urlId2);

        // Assert
        assertEquals("Url with id " + urlId1 + " has been deleted", result1);
        assertEquals("Url with id " + urlId2 + " has been deleted", result2);
        verify(urlRepo, times(1)).existsById(urlId1);
        verify(urlRepo, times(1)).existsById(urlId2);

    }


    @Test
    void deleteUrlById_ThrowingResourceNotFound() {
        // Arrange
        long urlId = 1L;
        when(urlRepo.existsById(urlId)).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(ResourceNotFound.class, () -> urlService.deleteUrlById(urlId));
        verify(urlRepo, never()).deleteByUrlId(urlId); // Verify delete method was never called
    }


    @Test
    void deleteAllUrls_Successful() {
        // Arrange
        User user1 = new User(1L, "Pavel", "password", "USER", null);
        User user2 = new User(2L, "NotPavel", "password", "ADMIN,USER", null);

        Url url1 = new Url(1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01", user1);
        Url url2 = new Url(2L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02", user2);
        when(urlRepo.findAll()).thenReturn(Arrays.asList(url1, url2));

        // Act
        String result = urlService.deleteAllUrls();

        // Assert
        assertEquals("All URLs have been deleted.", result);
        verify(urlRepo, times(1)).deleteAll();
    }


    @Test
    void deleteAllUrls_ThrowingResourceNotFound() {
        // Arrange
        // Empty list:

        // Act and Assert
        Assertions.assertThrows(ResourceNotFound.class, () -> urlService.deleteAllUrls());
        verify(urlRepo, never()).deleteAll();
    }


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


    @Test
    void checkUrlHealthById_Successful() {
        // Arrange
        User user1 = new User(1L, "Pavel", "password", "USER", null);

        Url url1 = new Url(1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01", user1);

        when(urlRepo.findByUrlId(1L)).thenReturn(Optional.of(url1));
        when(restTemplate.getForEntity("http://youtube.com", String.class))
                .thenReturn(new ResponseEntity<>("Healthy", HttpStatus.OK));


        // Act
        boolean result = urlService.checkUrlHealthById(1L);

        // Assert
        assertTrue(result);
    }


    @Test
    void checkUrlHealthById_ThrowingResourceNotFound() {
        // Arrange
        when(urlRepo.findByUrlId(1L)).thenReturn(Optional.empty());


        // Act and Assert
        assertThrows(ResourceNotFound.class, () -> urlService.checkUrlHealthById(1L));
    }


    @Test
    void checkAllUrlsHealth_Successful() {
        // Arrange
        User user1 = new User(1L, "Pavel", "password", "USER", null);
        User user2 = new User(2L, "NotPavel", "password", "ADMIN,USER", null);

        Url url1 = new Url(1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01", user1);
        Url url2 = new Url(2L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02", user2);
        Url url3 = new Url(2L, "Invalid Website", "http://invalid-url.com", true, "2023-02-02", "2023-02-02", user2);
        List<Url> urls = List.of(url1, url2, url3);

        when(urlRepo.findAll()).thenReturn(urls);
        when(restTemplate.getForEntity("http://youtube.com", String.class))
                .thenReturn(new ResponseEntity<>("Healthy", HttpStatus.OK));
        when(restTemplate.getForEntity("http://github.com", String.class))
                .thenReturn(new ResponseEntity<>("Healthy", HttpStatus.OK));
        when(restTemplate.getForEntity("http://invalid-url.com", String.class))
                .thenThrow(new ApiBadRequest("Invalid URL"));

        // Act
        urlService.checkAllUrlsHealth();

        // Assert
        verify(urlRepo, times(1)).findAll();
        assertTrue(url1.getUrlHealth());
        assertTrue(url2.getUrlHealth());
        assertFalse(url3.getUrlHealth());
    }


    @Test
    void checkAllUrlsHealth_ThrowingResourceNotFound() {
        // Arrange
        List<Url> emptyList = new ArrayList<>();
        when(urlRepo.findAll()).thenReturn(emptyList);

        // Act and Assert
        assertThrows(ResourceNotFound.class, () -> urlService.checkAllUrlsHealth());
    }


    @Test
    void editUrl_Successful() {
        // Arrange

        User user1 = new User(1L, "Pavel", "password", "USER", null);

        Url url1 = new Url(1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01", user1);

        UrlCreationDto urlCreateDto = new UrlCreationDto();
        urlCreateDto.setUrlName("YouTube Updated");
        urlCreateDto.setFullUrl("http://youtube-updated.com");

        when(urlRepo.findById(1L)).thenReturn(Optional.of(url1));
        when(urlRepo.findByFullUrl("http://youtube-updated.com")).thenReturn(Optional.empty());

        // Act
        urlService.editUrl(1L, urlCreateDto);

        // Assert
        verify(urlRepo, times(1)).save(url1);
        assertEquals("YouTube Updated", url1.getUrlName());
        assertEquals("http://youtube-updated.com", url1.getFullUrl());

    }

    @Test
    void editUrl_ThrowingResourceNotFound() {
        // Arrange
        UrlCreationDto urlCreateDto = new UrlCreationDto();
        urlCreateDto.setUrlName("YouTube Updated");
        urlCreateDto.setFullUrl("http://youtube-updated.com");

        when(urlRepo.findById(1L)).thenReturn(Optional.empty());


        // Act and Assert
        assertThrows(ResourceNotFound.class, () -> urlService.editUrl(1L, urlCreateDto));
    }


    @Test
    void editUrl_ThrowingConflict() {
        // Arrange
        User user1 = new User(1L, "Pavel", "password", "USER", null);
        User user2 = new User(2L, "NotPavel", "password", "ADMIN,USER", null);

        Url url1 = new Url(1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01", user1);
        Url url2 = new Url(2L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02", user2);



        UrlCreationDto urlCreateDto = new UrlCreationDto();
        urlCreateDto.setUrlName("YouTube Updated");
        urlCreateDto.setFullUrl("http://youtube-updated.com");

        when(urlRepo.findById(1L)).thenReturn(Optional.of(url1));
        when(urlRepo.findByFullUrl("http://youtube-updated.com")).thenReturn(Optional.of(url2));

        // Act and Assert
        assertThrows(Conflict.class, () -> urlService.editUrl(1L, urlCreateDto));
    }


}
