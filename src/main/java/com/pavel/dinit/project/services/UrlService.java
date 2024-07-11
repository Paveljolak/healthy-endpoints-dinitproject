package com.pavel.dinit.project.services;

import com.pavel.dinit.project.dtos.UrlCreationDto;
import com.pavel.dinit.project.dtos.UrlReadingDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UrlService {

    private static final String NO_URLS_STORED_ATM = "There are no URLs stored at the moment.";

    private final UrlRepo urlRepo;
    private final UserRepo userRepo;
    private final RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(UrlService.class);


    public UrlService(UrlRepo urlRepo, UserRepo userRepo, RestTemplate restTemplate) {
        this.urlRepo = urlRepo;
        this.userRepo = userRepo;
        this.restTemplate = restTemplate;
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
    public String deleteUrlById(Long urlId) {
        if (!urlRepo.existsById(urlId)) {
            throw new ResourceNotFound("There is no url with id " + urlId + ".");
        }
        urlRepo.deleteByUrlId(urlId);
        return "Url with id " + urlId + " has been deleted";
    }

    // Function to delete all URLs:
    public String deleteAllUrls() {
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
    public String addUrl(UrlCreationDto urlCreateDTO) {

    // Check full urls:
        if (urlCreateDTO.getFullUrl() == null || urlCreateDTO.getFullUrl().isEmpty()) {
            throw new Conflict("There is no URL specified.");
        } else if (UrlReadingDto.checkUrlValidity(urlCreateDTO.getFullUrl())) {
            throw new Conflict("Invalid URL format.");
        } else if (urlCreateDTO.getUrlName() == null || urlCreateDTO.getUrlName().isEmpty()) {
            throw new Conflict("There is no URL name specified.");
        } else if (urlCreateDTO.getAddedByUserId() == null) {
            throw new Conflict("There is no User Id specified.");
            // This will later be used for authentication.
    }

        // Check if user exists:
        User user = userRepo.findById(urlCreateDTO.getAddedByUserId())
                .orElseThrow(() -> new ResourceNotFound("User with ID " + urlCreateDTO.getAddedByUserId() + " not found."));

        // Check if url exists:
        Optional<Url> existingUrl = urlRepo.findByFullUrl(urlCreateDTO.getFullUrl());
        if (existingUrl.isPresent()) {
            throw new Conflict("URL already exists.");
        }

        String urlHealthStr = urlCreateDTO.getFullUrl();
        boolean urlHealth = checkUrlHealth1(urlHealthStr);
        Url url = UrlCreationDto.creationToUrlEnt(urlCreateDTO, urlHealth, user);
        urlRepo.save(url);

        return "Created URL with ID: " + url.getUrlId();
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
            logger.info("The healthy URL is:  {}", fullUrl);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.info("The unhealthy URL is:  {}", fullUrl);
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
    public void checkAllUrlsHealth() {
        List<Url> urls = urlRepo.findAll();

        if (urls.isEmpty()) {
            throw new ResourceNotFound(NO_URLS_STORED_ATM);
        }

        urls.forEach(url -> {
            boolean isHealthy = checkUrlHealth1(url.getFullUrl());
            url.setUrlHealth(isHealthy);

        });
        urlRepo.saveAll(urls);

    }


    public void editUrl(Long id, UrlCreationDto urlCreateDto) {
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




