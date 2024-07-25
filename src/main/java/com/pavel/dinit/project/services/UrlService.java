package com.pavel.dinit.project.services;

import com.pavel.dinit.project.dtos.UrlCreationDto;
import com.pavel.dinit.project.dtos.UrlReadingDto;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import com.pavel.dinit.project.exceptions.badrequest.ApiBadRequest;
import com.pavel.dinit.project.models.Url;
import com.pavel.dinit.project.models.User;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UrlService {

    private static final String NO_URLS_STORED_ATM = "There are no URLs stored at the moment.";

    private final UrlRepo urlRepo;
    private final UserRepo userRepo;
    private final RestTemplate restTemplate;
    private final AccessControlService accessControlService;
    private final AlertService alertService;

    Logger logger = LoggerFactory.getLogger(UrlService.class);


    public UrlService(UrlRepo urlRepo, UserRepo userRepo, RestTemplate restTemplate, AccessControlService accessControlService, AlertService alertService) {
        this.urlRepo = urlRepo;
        this.userRepo = userRepo;
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
        if (!accessControlService.canDelete(urlId, username)) {
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


    // Function to add a single URL:
    // This one will probably read some info that users would give
    // And then write that one:
    @Transactional
    public UrlReadingDto addUrl(UrlCreationDto urlCreateDTO, String username) {
        validateUrlCreationDto(urlCreateDTO);

        User user = getUserByUsername(username);

        checkIfUrlExists(urlCreateDTO.getFullUrl());

        boolean urlHealth = checkUrlHealth1(urlCreateDTO.getFullUrl());

        Url url = UrlCreationDto.creationToUrlEnt(urlCreateDTO, urlHealth, user);
        url.setDateAdded(String.valueOf(LocalDateTime.now())); // Set current timestamp

        urlRepo.save(url);

        // Convert Url to UrlReadingDto
        return UrlReadingDto.readingDtoFromUrl(url); // Use DTO to avoid large objects
    }



    private void validateUrlCreationDto(UrlCreationDto urlCreateDTO) {
        if (urlCreateDTO.getFullUrl() == null || urlCreateDTO.getFullUrl().isEmpty()) {
            throw new Conflict("There is no URL specified.");
        }
        if (UrlReadingDto.checkUrlValidity(urlCreateDTO.getFullUrl())) {
            throw new Conflict("Invalid URL format.");
        }
        if (urlCreateDTO.getUrlName() == null || urlCreateDTO.getUrlName().isEmpty()) {
            throw new Conflict("There is no URL name specified.");
        }
    }

    private User getUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found."));
    }

    private void checkIfUrlExists(String fullUrl) {
        Optional<Url> existingUrl = urlRepo.findByFullUrl(fullUrl);
        if (existingUrl.isPresent()) {
            throw new Conflict("URL already exists.");
        }
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
            e.printStackTrace();
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
    public boolean checkUrlHealthById(Long id) {
        UrlReadingDto url = getUrlById(id);
        String fullUrl = url.getFullUrl();
        return checkUrlHealth1(fullUrl);
    }


    // Function to check and update all the URLS healths, im not sure how to write this XD
    @Transactional
    public void checkAllUrlsHealth() {
        List<Url> urls = urlRepo.findAll();

        if (urls.isEmpty()) {
            throw new ResourceNotFound(NO_URLS_STORED_ATM);
        }

        urls.forEach(url -> {
            boolean isHealthy = checkUrlHealth1(url.getFullUrl());

            if (url.getUrlHealth() != isHealthy) {
                url.setUrlHealth(isHealthy);
                alertUser(url);
            }
            url.setLastChecked(LocalDateTime.now().toString());
        });
        urlRepo.saveAll(urls);
    }


    public void alertUser(Url url){
        User user = url.getAddedByUserId();
        if (user != null){
            alertService.sendEmail(user.getEmail(), "URL Health Status Update", "URL: " + url.getUrlName() + " health status changed to: " + url.getUrlHealth() + ".");
            logger.info("Sent email to users email: " + user.getEmail() + " for URL health status change.");
        }
    }

    public void editUrl(Long id, UrlCreationDto urlCreateDto, String username) {

        if (!accessControlService.canEdit(id, username)) {
            throw new SecurityException("You are not authorized to edit this URL.");
        }

        Optional<Url> optionalUrl = urlRepo.findById(id);
        if (optionalUrl.isPresent()) {
            Url url = optionalUrl.get();

            // Checking the name:
            if (urlCreateDto.getUrlName() != null && !urlCreateDto.getUrlName().isEmpty()) {
                url.setUrlName(urlCreateDto.getUrlName());
            } else {
                throw new Conflict("URL name must be specified.");
            }

            // Checking the FullUrl:
            if (urlCreateDto.getFullUrl() != null && !urlCreateDto.getFullUrl().isEmpty()) {

                if (!urlCreateDto.getFullUrl().equals(url.getFullUrl())) {
                    if (UrlReadingDto.checkUrlValidity(urlCreateDto.getFullUrl())) {
                        throw new Conflict("Invalid URL format.");
                    }

                    Optional<Url> existingUrl = urlRepo.findByFullUrl(urlCreateDto.getFullUrl());
                    if (existingUrl.isPresent()) {
                        throw new Conflict("URL already exists.");
                    }
                    url.setFullUrl(urlCreateDto.getFullUrl());
                }
            } else {
                throw new Conflict("There is no URL specified.");
            }

            urlRepo.save(url);

        } else {
            throw new ResourceNotFound("There is no url with id " + id + ".");
        }
    }


}




