package com.pavelDinit.dinitProject.service;

import com.pavelDinit.dinitProject.components.Mapper;
import com.pavelDinit.dinitProject.dtos.UrlCreationDto;
import com.pavelDinit.dinitProject.dtos.UrlReadingDto;
import com.pavelDinit.dinitProject.models.Url;
import com.pavelDinit.dinitProject.repo.UrlRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UrlService {

    private final UrlRepo urlRepo;
    private final Mapper mapper;

    public UrlService(UrlRepo urlRepo, Mapper mapper) {
        this.urlRepo = urlRepo;
        this.mapper = mapper;
    }

    // Function to get all the URLS in a list:
    public List<UrlReadingDto> getAllUrls() {
        List<Url> allUrls = urlRepo.findAll();
        if (allUrls.isEmpty()) {
            throw new RuntimeException("There are no URLs stored at the moment.");
        }
        return allUrls.stream().map(mapper::readingToDto).collect(Collectors.toList());
    }


    // Function to get a single URL by its name:
    public UrlReadingDto getUrlById(Long urlId){
        Url url = urlRepo.findByUrlId(urlId)
                .orElseThrow(() -> new RuntimeException("URL with name " + urlId + " not found."));
        return mapper.readingToDto(url);
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
        Url url = mapper.creationToUrl(urlCreateDTO);
        urlRepo.save(url);
        return "Created URL with ID: " + url.getUrlId();
    }



}




