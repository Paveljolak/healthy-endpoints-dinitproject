package com.pavel.dinit.project.controllers;

import com.pavel.dinit.project.dtos.UrlHealthHistoryDto;
import com.pavel.dinit.project.dtos.UrlReadingDto;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import com.pavel.dinit.project.services.AccessControlService;
import com.pavel.dinit.project.services.AuthenticationService;
import com.pavel.dinit.project.services.UrlHealthHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("health_history")
public class UrlHealthHistoryController {

    private static final String INVALID_ID = "Invalid ID: ";
    private final UrlHealthHistoryService urlHealthHistoryService;
    private final AccessControlService accessControlService;

    public UrlHealthHistoryController(UrlHealthHistoryService urlHealthHistoryService, AuthenticationService authenticationService, AccessControlService accessControlService) {
        this.urlHealthHistoryService = urlHealthHistoryService;
        this.accessControlService = accessControlService;
    }


    @GetMapping
    public ResponseEntity<List<UrlHealthHistoryDto>> getAllHealthHistories(Principal principal) {

        // Get the username from the Principal object
        String username = principal.getName();

        // Check if the user is an admin
        if (!accessControlService.isAdmin(username)) {
            throw new UnauthorizedException("User " + username + " is not authorized to access all health histories.");
        }

        List<UrlHealthHistoryDto> histories = urlHealthHistoryService.getAllHealthHistories();
        return ResponseEntity.ok(histories);
    }

    // Get health history by URL ID
    @GetMapping("/{urlId}")
    public ResponseEntity<List<UrlHealthHistoryDto>> getHealthHistoriesByUrlId(@PathVariable Long urlId) {
        List<UrlHealthHistoryDto> histories = urlHealthHistoryService.getHealthHistoriesByUrlId(urlId);
        return ResponseEntity.ok(histories);
    }


    @PostMapping("/{urlId}")
    public ResponseEntity<String> addHealthHistory(@PathVariable Long urlId, Principal principal) {
        String username = principal.getName();
        urlHealthHistoryService.addHealthHistory(urlId, username);
        return ResponseEntity.ok("Health history added successfully.");
    }

    @DeleteMapping("/{urlId}")
    public ResponseEntity<Void> deleteHealthHistory(@PathVariable Long urlId, Principal principal) {
        // Get the username from the Principal object
        String username = principal.getName();

        // Call the service to delete the health history record
        urlHealthHistoryService.deleteHealthHistoryById(urlId, username);

        return ResponseEntity.noContent().build();
    }

}
