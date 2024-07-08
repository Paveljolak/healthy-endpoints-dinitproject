package com.pavelDinit.dinitProject.Controllers;

import com.pavelDinit.dinitProject.dtos.UrlCreationDto;
import com.pavelDinit.dinitProject.dtos.UrlReadingDto;
import com.pavelDinit.dinitProject.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("urls")
public class UrlController {


    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping
        public List<UrlReadingDto> getAllUrls(){
        return urlService.getAllUrls();
    }

    // Requesting all the URL'S names:
    // Only for quick testing, will delete later:
    @GetMapping("/names")
    public List<Object> getUrlNames() {
        return urlService.getAllUrlNames();
    }

    // Requesting a single URL based on its ID:
    @GetMapping("/{id}")
    public UrlReadingDto getUrlById(@PathVariable Long id) {
        return urlService.getUrlById(id);
    }

    // Requesting the health of a single URL based on its ID:
    @GetMapping("/{id}/health")
    public ResponseEntity<String> checkUrlHealth(@PathVariable Long id) {
        UrlReadingDto url = urlService.getUrlById(id);
        String fullUrl = url.getFullUrl();

        boolean isHealthy = urlService.checkUrlHealth1(fullUrl);

        if (isHealthy) {
            return ResponseEntity.ok("URL is healthy");
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("URL is not healthy");
        }
    }

    // Requesting a deletion of a single URL based on the ID:
    @DeleteMapping("/{id}")
    public String deleteUrlById(@PathVariable Long id) {
        return urlService.deleteUrlById(id);
    }

    // Requesting creation of a new single URL based on the NAME:
    @PostMapping
    public String addUrl(@RequestBody UrlCreationDto createDto){
        return urlService.addUrl(createDto);
    }

    @PatchMapping("/checkHealthAll")
    public ResponseEntity<String> checkAllUrlsHealth(){
        urlService.checkAllUrlsHealth();
        return ResponseEntity.ok("Health check initiated for all URLs.");
    }



}
