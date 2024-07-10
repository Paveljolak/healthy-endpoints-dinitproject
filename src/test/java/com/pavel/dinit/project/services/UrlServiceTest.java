package com.pavelDinit.dinitProject.services;

import com.pavelDinit.dinitProject.dtos.UrlCreationDto;
import com.pavelDinit.dinitProject.dtos.UrlReadingDto;
import com.pavelDinit.dinitProject.exceptions.badrequest.ApiBadRequest;
import com.pavelDinit.dinitProject.exceptions.conflict.Conflict;
import com.pavelDinit.dinitProject.exceptions.notfound.ResourceNotFound;
import com.pavelDinit.dinitProject.models.Url;
import com.pavelDinit.dinitProject.repo.UrlRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private RestTemplate restTemplate;

    @InjectMocks
    private UrlService urlService;


    @Test
    public void getAllUrls_Successful() {
        // Arrange
        Url url1 = new Url(1L, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");
        Url url2 = new Url(2L, 1L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02");

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
    public void getUrlById_Successful() {
        // Arrange
        Long urlId1 = 1L;
        Long urlId2 = 2L;
        Url url1 = new Url(urlId1, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");
        Url url2 = new Url(urlId2, 1L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02");

        // Mock the behavior of urlService.getUrlById
        when(urlRepo.findByUrlId(urlId1)).thenReturn(Optional.of(url1));
        when(urlRepo.findByUrlId(urlId2)).thenReturn(Optional.of(url2));


        // Act
        UrlReadingDto urlReadDto1 = urlService.getUrlById(urlId1);
        UrlReadingDto urlReadDto2 = urlService.getUrlById(urlId2);

        // Assert
        assertEquals(url1.getUrlId(), urlReadDto1.getUrlId());
        assertEquals(url2.getUrlId(), urlReadDto2.getUrlId());

        assertEquals(url1.getAddedByUserId(), urlReadDto1.getAddedByUserId());
        assertEquals(url2.getAddedByUserId(), urlReadDto2.getAddedByUserId());


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
    public void getUrlById_ThrowingResourceNotFound() {
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
    public void getAllUrlNames_Successful() {
        // Arrange
        Url url1 = new Url(1L, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");
        Url url2 = new Url(2L, 1L, "GitHub", "http://github.com", true, "2023-01-01", "2023-02-02");

        when(urlRepo.findAll()).thenReturn(List.of(url1, url2));


        // Act
        List<Object> result = urlService.getAllUrlNames();


        // Assert
        assertEquals(2, result.size());
        assertEquals("YouTube", result.get(0));
        assertEquals("GitHub", result.get(1));

    }


    @Test
    public void getAllUrlNames_ThrowingResourceNotFound() {
        // Arrange
        // Mock empty list:
        List<Url> listOfUrls = new ArrayList<>();

        // Act and Assert
        Assertions.assertThrows(ResourceNotFound.class, () -> urlService.getAllUrlNames());
    }

    @Test
    public void deleteUrlById_Successful() {
        // Arrange
        Long urlId1 = 1L;
        Long urlId2 = 2L;

        Url url1 = new Url(urlId1, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");
        Url url2 = new Url(urlId2, 1L, "GitHub", "http://github.com", true, "2023-01-01", "2023-01-01");

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
    public void deleteUrlById_ThrowingResourceNotFound() {
        // Arrange
        long urlId = 1L;
        when(urlRepo.existsById(urlId)).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(ResourceNotFound.class, () -> urlService.deleteUrlById(urlId));
        verify(urlRepo, never()).deleteByUrlId(urlId); // Verify delete method was never called
    }


    @Test
    public void deleteAllUrls_Successful() {
        // Arrange
        Url url1 = new Url(1L, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");
        Url url2 = new Url(2L, 1L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02");
        when(urlRepo.findAll()).thenReturn(Arrays.asList(url1, url2));

        // Act
        String result = urlService.deleteAllUrls();

        // Assert
        assertEquals("All URLs have been deleted.", result);
        verify(urlRepo, times(1)).deleteAll();
    }


    @Test
    public void deleteAllUrls_ThrowingResourceNotFound() {
        // Arrange
        // Empty list:
        List<Url> listOfUrls = new ArrayList<>();

        // Act and Assert
        Assertions.assertThrows(ResourceNotFound.class, () -> urlService.deleteAllUrls());
        verify(urlRepo, never()).deleteAll();
    }

    @Test
    public void addUrl_Successful() {
        // Arrange
        UrlCreationDto urlCreateDto = new UrlCreationDto();
        urlCreateDto.setUrlName("YouTube");
        urlCreateDto.setFullUrl("http://youtube.com");
        urlCreateDto.setAddedByUserId(1L);


        when(urlRepo.findByFullUrl(urlCreateDto.getFullUrl())).thenReturn(Optional.empty());
        when(urlRepo.save(any(Url.class))).thenAnswer(invocation -> {
            Url url = invocation.getArgument(0);
            url.setUrlId(1L);
            return url;
        });

        // Act
        String result = urlService.addUrl(urlCreateDto);

        // Assert
        assertEquals("Created URL with ID: 1", result);
        verify(urlRepo, times(1)).findByFullUrl(urlCreateDto.getFullUrl());
        verify(urlRepo, times(1)).save(any(Url.class));
    }

    @Test
    public void addUrl_ThrowingConflict() {
        // Arrange
        UrlCreationDto urlCreateDto = new UrlCreationDto();
        urlCreateDto.setUrlName("YouTube");
        urlCreateDto.setFullUrl("http://youtube.com");
        urlCreateDto.setAddedByUserId(1L);

        when(urlRepo.findByFullUrl(urlCreateDto.getFullUrl())).thenReturn(Optional.of(new Url()));


        // Act and Assert
        Assertions.assertThrows(Conflict.class, () -> urlService.addUrl(urlCreateDto));
        verify(urlRepo, times(1)).findByFullUrl(urlCreateDto.getFullUrl());
        verify(urlRepo, never()).save(any(Url.class));
    }

    @Test
    public void checkUrlHealth1_Successful() {
        // Arrange
        Url url1 = new Url(1L, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");
        Url url2 = new Url(2L, 1L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02");

        when(restTemplate.getForEntity("http://youtube.com", String.class))
                .thenReturn((new ResponseEntity<>("Healthy", HttpStatus.OK)));

        // Act
        boolean result = urlService.checkUrlHealth1("http://youtube.com");


        // Assert
        assertTrue(result);
    }


    @Test
    public void checkUrlHealth1_ThrowingConflict() {
        // Arrange
        when(restTemplate.getForEntity("http://invalid-url.com", String.class)).thenThrow(new RuntimeException());

        // Act
        boolean result = urlService.checkUrlHealth1("http://invalid-url.com");

        // Assert
        assertFalse(result);

    }


    @Test
    public void checkUrlHealthById_Successful() {
        // Arrange
        Url url1 = new Url(1L, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");

        when(urlRepo.findByUrlId(1L)).thenReturn(Optional.of(url1));
        when(restTemplate.getForEntity("http://youtube.com", String.class))
                .thenReturn(new ResponseEntity<>("Healthy", HttpStatus.OK));


        // Act
        boolean result = urlService.checkUrlHealthById(1L);

        // Assert
        assertTrue(result);
    }


    @Test
    public void checkUrlHealthById_ThrowingResourceNotFound() {
        // Arrange
        when(urlRepo.findByUrlId(1L)).thenReturn(Optional.empty());


        // Act and Assert
        assertThrows(ResourceNotFound.class, () -> urlService.checkUrlHealthById(1L));
    }


    @Test
    public void checkAllUrlsHealth_Successful() {
        // Arrange
        Url url1 = new Url(1L, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");
        Url url2 = new Url(2L, 1L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02");
        Url url3 = new Url(2L, 1L, "Invalid Website", "http://invalid-url.com", true, "2023-02-02", "2023-02-02");
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
    public void checkAllUrlsHealth_ThrowingResourceNotFound() {
        // Arrange
        List<Url> emptyList = new ArrayList<>();
        when(urlRepo.findAll()).thenReturn(emptyList);

        // Act and Assert
        assertThrows(ResourceNotFound.class, () -> urlService.checkAllUrlsHealth());
    }


    @Test
    public void editUrl_Successful() {
        // Arrange
        Url url1 = new Url(1L, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");

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
    public void editUrl_ThrowingResourceNotFound() {
        // Arrange
        UrlCreationDto urlCreateDto = new UrlCreationDto();
        urlCreateDto.setUrlName("YouTube Updated");
        urlCreateDto.setFullUrl("http://youtube-updated.com");

        when(urlRepo.findById(1L)).thenReturn(Optional.empty());


        // Act and Assert
        assertThrows(ResourceNotFound.class, () -> urlService.editUrl(1L, urlCreateDto));
    }


    @Test
    public void editUrl_ThrowingConflict() {
        // Arrange
        Url url1 = new Url(1L, 1L, "YouTube", "http://youtube.com", true, "2023-01-01", "2023-01-01");
        Url url2 = new Url(2L, 1L, "GitHub", "http://github.com", true, "2023-02-02", "2023-02-02");

        UrlCreationDto urlCreateDto = new UrlCreationDto();
        urlCreateDto.setUrlName("YouTube Updated");
        urlCreateDto.setFullUrl("http://youtube-updated.com");

        when(urlRepo.findById(1L)).thenReturn(Optional.of(url1));
        when(urlRepo.findByFullUrl("http://youtube-updated.com")).thenReturn(Optional.of(url2));

        // Act and Assert
        assertThrows(Conflict.class, () -> urlService.editUrl(1L, urlCreateDto));
    }



}
