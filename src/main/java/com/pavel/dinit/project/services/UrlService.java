package com.pavel.dinit.project.services;

import com.pavel.dinit.project.dtos.UrlCreationDto;
import com.pavel.dinit.project.dtos.UrlReadingDto;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import com.pavel.dinit.project.exceptions.badrequest.ApiBadRequest;
import com.pavel.dinit.project.models.Url;
import com.pavel.dinit.project.models.UrlHealthHistory;
import com.pavel.dinit.project.models.User;
import com.pavel.dinit.project.repo.UrlHealthHistoryRepo;
import com.pavel.dinit.project.repo.UrlRepo;
import com.pavel.dinit.project.exceptions.badrequest.TypeMissmatch;
import com.pavel.dinit.project.exceptions.conflict.Conflict;
import com.pavel.dinit.project.exceptions.notfound.ResourceNotFound;
import com.pavel.dinit.project.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pavel.dinit.project.dtos.UrlReadingDto.checkUrlValidity;

@Service
public class UrlService {

    private static final String NO_URLS_STORED_ATM = "There are no URLs stored at the moment.";
    private static final String URL_ALREADY_EXISTS = "URL already exists.";
    private static final String INVALID_URL_FORMAT = "Invalid URL format.";


    private final UrlRepo urlRepo;
    private final UserRepo userRepo;
    private final UrlHealthHistoryRepo urlHealthHistoryRepo;
    private final RestTemplate restTemplate;
    private final AccessControlService accessControlService;
    private final AlertService alertService;

    Logger logger = LoggerFactory.getLogger(UrlService.class);


    public UrlService(UrlRepo urlRepo, UserRepo userRepo, UrlHealthHistoryRepo urlHealthHistoryRepo, RestTemplate restTemplate, AccessControlService accessControlService, AlertService alertService) {
        this.urlRepo = urlRepo;
        this.userRepo = userRepo;
        this.urlHealthHistoryRepo = urlHealthHistoryRepo;
        this.restTemplate = restTemplate;
        this.accessControlService = accessControlService;
        this.alertService = alertService;
    }

    // Function to get all the URLS in a list:
    public List<UrlReadingDto> getAllUrls() {
        List<Url> allUrls = urlRepo.findAll();
        if (allUrls.isEmpty()) {
            throw new ResourceNotFound(NO_URLS_STORED_ATM);
        }
        return allUrls.stream().map(UrlReadingDto::readingDtoFromUrl).toList();

    }


    // Function to get a single URL by its name:
    public UrlReadingDto getUrlById(Long urlId) {

        if (urlId == null || urlId <= 0) {
            throw new TypeMissmatch("Invalid URL ID: " + urlId);
        }

        Url url = urlRepo.findByUrlId(urlId).orElseThrow(() -> new ResourceNotFound("URL with name " + urlId + " not found."));
        return UrlReadingDto.readingDtoFromUrl(url);
    }

    // Function to get all the names of the URLS that are inside the URL list:
    public List<Object> getAllUrlNames() {
        List<UrlReadingDto> all = getAllUrls();
        if (all.isEmpty()) {
            throw new ResourceNotFound(NO_URLS_STORED_ATM);
        }
        return all.stream().map(UrlReadingDto::getUrlName).collect(Collectors.toList());
    }

    // Function to delete a single URL based on its ID:
    @Transactional
    public ResponseEntity<Map<String, String>> deleteUrlById(Long urlId, String username) {
        if (!accessControlService.canAlter(urlId, username)) {
            throw new UnauthorizedException("You are not authorized to delete this URL.");
        }

        if (!urlRepo.existsById(urlId)) {
            throw new ResourceNotFound("There is no URL with id " + urlId + ".");
        }
        urlRepo.deleteById(urlId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "URL with id " + urlId + " has been deleted");

        return ResponseEntity.ok(response);
    }

    // Function to delete all URLs:
    public String deleteAllUrls(String username) {
        if (!accessControlService.isAdmin(username)) {
            throw new UnauthorizedException("Only admins can delete all URLs.");
        }

        List<Url> allUrls = urlRepo.findAll();
        if (allUrls.isEmpty()) {
            throw new ResourceNotFound(NO_URLS_STORED_ATM);
        }
        urlRepo.deleteAll();
        return "All URLs have been deleted.";
    }


    @Transactional
    public UrlReadingDto addUrl(UrlCreationDto urlCreateDTO, String username) {
        validateUrlCreationDto(urlCreateDTO); // Validate the DTO
        User user = getUserByUsername(username); // Get the user making the request
        String fullUrl = urlCreateDTO.getFullUrl(); // Get the URL to be added

        // Validate the URL format
        if (!checkUrlValidity(fullUrl)) {
            throw new Conflict(INVALID_URL_FORMAT);
        }

        // Check if the URL already exists
        if (urlRepo.findByFullUrl(fullUrl).isPresent()) {
            throw new Conflict(URL_ALREADY_EXISTS);
        }

        // Check the URL health
        boolean urlHealth = checkUrlHealth1(fullUrl);

        // Create the URL entity and save it
        Url url = UrlCreationDto.creationToUrlEnt(urlCreateDTO, urlHealth, user);
        url.setDateAdded(String.valueOf(LocalDateTime.now())); // Set current timestamp
        urlRepo.save(url);

        // Return the URL reading DTO
        return UrlReadingDto.readingDtoFromUrl(url);
    }


    private void validateUrlCreationDto(UrlCreationDto urlCreateDTO) {
        if (urlCreateDTO.getFullUrl() == null || urlCreateDTO.getFullUrl().isEmpty()) {
            throw new Conflict("There is no URL specified.");
        }
        if (!checkUrlValidity(urlCreateDTO.getFullUrl())) {
            throw new Conflict(INVALID_URL_FORMAT);
        }
        if (urlCreateDTO.getUrlName() == null || urlCreateDTO.getUrlName().isEmpty()) {
            throw new Conflict("There is no URL name specified.");
        }
    }

    private User getUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found."));
    }

    // Function for checking if an url is healthy or not.
// Checks inside for Healthy status otherwise it will give us false.
    // We are not using this one for now, but SolarLint is annoying me, so I uncommented it
    public boolean checkUrlHealth(String fullUrl) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                return responseBody != null && responseBody.contains("\"status\":\"Healthy\"");
            } else {
                return false;
            }
        } catch (ApiBadRequest e) {
            logger.error("API request failed for URL: {}", fullUrl, e);
            return false;
        }
    }

    // Function that checks if we are receiving 200 from an url, if we are it will give 200
// This is for testing purposes, later we switch to checkUrlHealth function that is currently commented above.
    public boolean checkUrlHealth1(String fullUrl) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    // Function to return if url is healthy or not:
    @Transactional
    public boolean checkUrlHealthById(Long id) {
        // Fetch URL using DTO
        Url url = urlRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("URL with ID " + id + " not found."));

        String fullUrl = url.getFullUrl();
        boolean health = checkUrlHealth1(fullUrl);

        // Update the URL with the latest health status
        url.setUrlHealth(health);
        url.setLastChecked(LocalDateTime.now().toString());
        urlRepo.save(url);

        // Record health status in the history table
        UrlHealthHistory history = new UrlHealthHistory();
        history.setUrlId(url); // Set the URL entity reference
        history.setHealthStatus(health);
        history.setTimestamp(LocalDateTime.now());

        urlHealthHistoryRepo.save(history);

        return health;
    }


    // Function to check and update all the URLS healths, im not sure how to write this XD
    @Transactional
    public void checkAllUrlsHealth() {
        List<Url> urls = urlRepo.findAll();

        if (urls.isEmpty()) {
            throw new ResourceNotFound(NO_URLS_STORED_ATM);
        }

        urls.forEach(url -> {
            // Check the health of the URL
            boolean isHealthy = checkUrlHealth1(url.getFullUrl());
            boolean previousHealthStatus = url.getUrlHealth();


            // Update URL health status and last checked timestamp
            url.setUrlHealth(isHealthy);
            url.setLastChecked(LocalDateTime.now().toString());

            // Save the updated URL
            urlRepo.save(url);

            // Record health status in the history table
            UrlHealthHistory history = new UrlHealthHistory();
            history.setUrlId(url); // Set the URL entity reference
            history.setHealthStatus(isHealthy);
            history.setTimestamp(LocalDateTime.now());

            urlHealthHistoryRepo.save(history);

            // Alert the user about the health status change
            if (previousHealthStatus != isHealthy) {
                alertUser(url);
            }
        });
    }


    public void alertUser(Url url) {
        User user = url.getAddedByUserId();
        if (user != null) {
            alertService.sendEmail(user.getEmail(), "URL Health Status Update", "URL: " + url.getUrlName() + " health status changed to: " + url.getUrlHealth() + ".");
            logger.info("Sent email to user's email: {} for URL health status change.", user.getEmail());

        }
    }

    public void editUrl(Long id, UrlCreationDto urlCreateDto, String username) {

        // Check access control
        if (!accessControlService.canAlter(id, username)) {
            throw new UnauthorizedException("You are not authorized to edit this URL.");
        }

        // Fetch the URL to be updated
        Url url = urlRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("There is no URL with id " + id + "."));

        // Update the URL name
        String newName = urlCreateDto.getUrlName();
        if (newName == null || newName.isEmpty()) {
            throw new Conflict("URL name must be specified.");
        }
        url.setUrlName(newName);

        // Update the Full URL
        String newFullUrl = urlCreateDto.getFullUrl();
        if (newFullUrl != null && !newFullUrl.isEmpty()) {
            String currentFullUrl = url.getFullUrl();

            // Only validate if the new URL is different from the current one
            if (!newFullUrl.equals(currentFullUrl)) {
                if (!checkUrlValidity(newFullUrl)) {
                    throw new Conflict(INVALID_URL_FORMAT);
                }

                boolean urlExists = urlRepo.findByFullUrl(newFullUrl).isPresent();
                if (urlExists) {
                    throw new Conflict(URL_ALREADY_EXISTS);
                }

                url.setFullUrl(newFullUrl);
            }
        } else {
            throw new Conflict("There is no URL specified.");
        }

        // Save the updated URL
        urlRepo.save(url);
    }


}




