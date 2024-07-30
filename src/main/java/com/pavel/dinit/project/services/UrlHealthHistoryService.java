package com.pavel.dinit.project.services;

import com.pavel.dinit.project.dtos.HealthHistoryResponseDto;
import com.pavel.dinit.project.dtos.UrlHealthHistoryDto;
import com.pavel.dinit.project.exceptions.notfound.ResourceNotFound;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import com.pavel.dinit.project.models.Url;
import com.pavel.dinit.project.models.UrlHealthHistory;
import com.pavel.dinit.project.repo.UrlHealthHistoryRepo;
import com.pavel.dinit.project.repo.UrlRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UrlHealthHistoryService {


    Logger logger = LoggerFactory.getLogger(UrlHealthHistoryService.class);


    public final UrlHealthHistoryRepo urlHealthHistoryRepo;
    public final UrlRepo urlRepo;
    private final AccessControlService accessControlService;


    public UrlHealthHistoryService(UrlHealthHistoryRepo urlHealthHistoryRepo1, UrlRepo urlRepo, AccessControlService accessControlService) {
        this.urlHealthHistoryRepo = urlHealthHistoryRepo1;
        this.urlRepo = urlRepo;
        this.accessControlService = accessControlService;
    }



    // Get all health history records
    public List<UrlHealthHistoryDto> getAllHealthHistories() {
        List<UrlHealthHistory> histories = urlHealthHistoryRepo.findAll();
        return histories.stream()
                .map(UrlHealthHistoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Get health history by URL ID
    public List<UrlHealthHistoryDto> getHealthHistoriesByUrlId(Long urlId) {
        Url url = urlRepo.findById(urlId)
                .orElseThrow(() -> new ResourceNotFound("URL with ID " + urlId + " not found."));

        List<UrlHealthHistory> histories = urlHealthHistoryRepo.findByUrlId(url);
        return histories.stream()
                .map(UrlHealthHistoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addHealthHistory(Long urlId, String username) {
        // Check if the URL exists
        Url url = urlRepo.findById(urlId)
                .orElseThrow(() -> new ResourceNotFound("URL with ID " + urlId + " not found."));

        // Check if the user is authorized
        if (!accessControlService.canAlter(urlId, username)) {
            throw new UnauthorizedException("User " + username + " is not authorized to add health history for URL ID " + urlId);
        }

        // Get the current health status
        boolean healthStatus = url.getUrlHealth();
        LocalDateTime timestamp = LocalDateTime.parse(url.getLastChecked()); // Use the URL's lastChecked timestamp

        // Check if a similar record already exists
        boolean alreadyExists = urlHealthHistoryRepo.existsByHealthStatusAndTimestamp(healthStatus, timestamp);
        if (alreadyExists) {
            // Log and return a message that the record already exists
            logger.info("Health history for URL ID {} with status {} and timestamp {} already exists.", urlId, healthStatus, timestamp);

            // Prepare response DTO
            HealthHistoryResponseDto response = new HealthHistoryResponseDto();
            response.setUrlId(urlId);
            response.setHealthStatus(healthStatus);
            response.setTimestamp(timestamp.toString());
            response.setMessage("Health history record already exists for URL ID " + urlId);

            return; // Return response indicating the record exists
        }

        // Create a new UrlHealthHistory record
        UrlHealthHistory history = new UrlHealthHistory();
        history.setUrlId(url); // Link to the URL entity
        history.setHealthStatus(healthStatus);
        history.setTimestamp(timestamp);

        // Save the record
        urlHealthHistoryRepo.save(history);

        // Prepare response DTO
        HealthHistoryResponseDto response = new HealthHistoryResponseDto();
        response.setUrlId(urlId);
        response.setHealthStatus(healthStatus);
        response.setTimestamp(timestamp.toString());
        response.setMessage("Health history added successfully."); // Indicate successful addition

        // Log the action
        logger.info("Added health history for URL ID {}: Status - {}, Timestamp - {}", urlId, healthStatus, timestamp);

    }



    @Transactional
    public void deleteHealthHistoryById(Long id, String username) {
        // Check if the health history record exists
        UrlHealthHistory history = urlHealthHistoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Health history with ID " + id + " not found."));

        Url url = history.getUrlId();

        // Use AccessControlService to check if the user has permission
        if (!accessControlService.canAlter(url.getUrlId(), username)) {
            throw new UnauthorizedException("User does not have permission to delete this health history record.");
        }

        // Delete the record
        urlHealthHistoryRepo.deleteById(id);
    }







}
