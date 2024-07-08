package com.pavelDinit.dinitProject.service;

import com.pavelDinit.dinitProject.dtos.UrlCreationDto;
import com.pavelDinit.dinitProject.dtos.UrlReadingDto;
import com.pavelDinit.dinitProject.models.Url;
import com.pavelDinit.dinitProject.repo.UrlRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UrlService {

    private final UrlRepo urlRepo;
    private final RestTemplate restTemplate;

    public UrlService(UrlRepo urlRepo, RestTemplate restTemplate) {
        this.urlRepo = urlRepo;
        this.restTemplate = restTemplate;
    }


    // Function to get all the URLS in a list:
    public List<UrlReadingDto> getAllUrls() {
        List<Url> allUrls = urlRepo.findAll();
        if (allUrls.isEmpty()) {
            throw new RuntimeException("There are no URLs stored at the moment.");
        }
        return allUrls.stream().map(UrlReadingDto::readingDtoFromUrl).collect(Collectors.toList());
    }


    // Function to get a single URL by its name:
    public UrlReadingDto getUrlById(Long urlId){
        Url url = urlRepo.findByUrlId(urlId)
                .orElseThrow(() -> new RuntimeException("URL with name " + urlId + " not found."));
        return UrlReadingDto.readingDtoFromUrl(url);
    }

    // Function to get all the names of the URLS that are inside the URL list:
    public List<Object> getAllUrlNames(){
        List<UrlReadingDto> all = getAllUrls();
        if(all.isEmpty()){
            throw new RuntimeException("There are no urls stored at the moment.");
        }
            return all.stream().map(UrlReadingDto::getUrlName).collect(Collectors.toList());
    }

    // Function to delete a single URL based on its ID:
    public String deleteUrlById(Long urlId) {
        if(!urlRepo.existsById(urlId)){
            throw new RuntimeException("There is no url with id " + urlId + ".");
        }
        urlRepo.deleteByUrlId(urlId);
        return "Url with id " + urlId + " has been deleted";
    }

    // Function to add a single URL:
    // This one will probably read some info that users would give
    // And then write that one:
    public String addUrl(UrlCreationDto urlCreateDTO) {
        String urlHealthStr = urlCreateDTO.getFullUrl();
        boolean urlHealth = false;
        urlHealth = checkUrlHealth1(urlHealthStr);
        Url url = UrlCreationDto.creationToUrlEnt(urlCreateDTO, urlHealth);
        urlRepo.save(url);
        return "Created URL with ID: " + url.getUrlId();
    }

    // Function for checking if an url is healthy or not.
    // Checks inside for Healthy status otherwise it will give us false.
//    public boolean checkUrlHealth(String fullUrl) {
//        try {
//            ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                String responseBody = response.getBody();
//                return responseBody != null && responseBody.contains("\"status\":\"Healthy\"");
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    // Function that checks if we are receiving 200 from an url, if we are it will give 200
    // This is for testing purposes, later we switch to checkUrlHealth function that is currently commented above.
    public boolean checkUrlHealth1(String fullUrl) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
//            return response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() < 300; // deprecated
                 System.out.println("The healthy URL is:  " + fullUrl);
                return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.out.println("The unhealthy URL is:  " + fullUrl);
            return false;
        }
    }


    // Function to check and update all the URLS healths, im not sure how to write this XD
    public void checkAllUrlsHealth(){
        List<Url> urls = urlRepo.findAll();
        urls.forEach(url -> {
            boolean isHealthy = checkUrlHealth1(url.getFullUrl());
            url.setUrlHealth(isHealthy);

        });
        urlRepo.saveAll(urls);

    }

    @Scheduled(fixedDelayString = "PT5M")
    public void checkAllUrlsHealthScheduler(){
        try {
            checkAllUrlsHealth();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}




