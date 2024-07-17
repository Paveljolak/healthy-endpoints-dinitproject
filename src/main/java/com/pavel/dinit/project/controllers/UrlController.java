package com.pavel.dinit.project.controllers;

import com.pavel.dinit.project.dtos.UrlCreationDto;
import com.pavel.dinit.project.dtos.UrlReadingDto;
import com.pavel.dinit.project.exceptions.badrequest.TypeMissmatch;
import com.pavel.dinit.project.services.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestController
@Validated
@RequestMapping("urls")
public class UrlController {

    private static final String INVALID_URLID = "Invalid URL ID: ";
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping
    public List<UrlReadingDto> getAllUrls() {
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
    public ResponseEntity<UrlReadingDto> getUrlById(@PathVariable String id) {
        try {
            Long urlId = Long.parseLong(id);
            UrlReadingDto urlReadingDto = urlService.getUrlById(urlId);
            return ResponseEntity.ok(urlReadingDto);
        } catch (NumberFormatException ex) {
            throw new TypeMissmatch(INVALID_URLID + id);
        } catch (MethodArgumentTypeMismatchException ex) {
            throw new TypeMissmatch(INVALID_URLID + ex.getValue());
        }
    }

    // Requesting the health of a single URL based on its ID:
    @GetMapping("/{id}/health")
    public ResponseEntity<String> checkUrlHealth(@PathVariable String id) {
        try {
            Long urlId = Long.parseLong(id);
            boolean isHealthy = urlService.checkUrlHealthById(urlId);
            if (isHealthy) {
                return ResponseEntity.ok("URL is healthy");
            } else {
                return ResponseEntity.status(503).body("URL is not healthy");
            }
        } catch (NumberFormatException ex) {
            throw new TypeMissmatch(INVALID_URLID + id);
        } catch (MethodArgumentTypeMismatchException ex) {
            throw new TypeMissmatch(INVALID_URLID + ex.getValue());
        }
    }

    // Requesting a deletion of a single URL based on the ID:
    @DeleteMapping("/{id}")
    public String deleteUrlById(@PathVariable String id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            Long urlId = Long.parseLong(id);
            return urlService.deleteUrlById(urlId, username);
        } catch (NumberFormatException ex) {
            throw new TypeMissmatch(INVALID_URLID + id);
        } catch (MethodArgumentTypeMismatchException ex) {
            throw new TypeMissmatch(INVALID_URLID + ex.getValue());
        }
    }

    // Requesting a deletion of a single URL based on the ID:
    @DeleteMapping
    public String deleteAllUrls() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        return urlService.deleteAllUrls(username);
    }

    // Requesting creation of a new single URL based on the NAME:
    @PostMapping
    public String addUrl(@RequestBody UrlCreationDto createDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return urlService.addUrl(createDto, username);
    }

    // Request to check the health of all URLs inside the DB:
    // This will be deleted later, since this function will be used in service:
    @PatchMapping("/checkHealthAll")
    public ResponseEntity<String> checkAllUrlsHealth() {
        urlService.checkAllUrlsHealth();
        return ResponseEntity.ok("Health check initiated for all URLs.");
    }

    // Request for editing a single URL based on its ID:
    @PutMapping("/{id}")
    public ResponseEntity<Void> editUrl(@PathVariable String id, @RequestBody UrlCreationDto createDto) {


        try {
            Long urlId = Long.parseLong(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            urlService.editUrl(urlId, createDto, username);
            return ResponseEntity.noContent().build();
        } catch (NumberFormatException ex) {
            throw new TypeMissmatch(INVALID_URLID + id);
        } catch (MethodArgumentTypeMismatchException ex) {
            throw new TypeMissmatch(INVALID_URLID + ex.getValue());
        }

    }

}
