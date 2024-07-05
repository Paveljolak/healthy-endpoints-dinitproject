package com.pavelDinit.dinitProject.components;

import com.pavelDinit.dinitProject.dtos.UrlCreationDto;
import com.pavelDinit.dinitProject.dtos.UrlReadingDto;
import com.pavelDinit.dinitProject.models.Url;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public UrlReadingDto readingToDto(Url url) {
        Long urlId = url.getUrlId();
        Long addedByUserId = url.getAddedByUserId();
        String name = url.getUrlName();
        String fullUrl = url.getFullUrl();
        Boolean urlHealth = url.getUrlHealth();
        String lastChecked = url.getLastChecked();

        return new UrlReadingDto(urlId, addedByUserId, name, fullUrl, urlHealth, lastChecked);
    }


    public Url readingToUrl(UrlReadingDto urlGetDTO){
        Url url = new Url();
        url.setUrlName(urlGetDTO.getUrlName());
        url.setFullUrl(urlGetDTO.getFullUrl());
        url.setUrlHealth(urlGetDTO.getUrlHealth());

        return url;
    }

    public UrlCreationDto creationToDto(Url url){
        Long addedByUserId = url.getAddedByUserId();
        String urlName = url.getUrlName();
        String fullUrl = url.getFullUrl();
        Boolean urlHealth = url.getUrlHealth();

        return new UrlCreationDto(addedByUserId, urlName, fullUrl, urlHealth);
    }

    public Url creationToUrl(UrlCreationDto urlCreateDTO){
        Url url = new Url();
        url.setAddedByUserId(urlCreateDTO.getAddedByUserId());
        url.setUrlName(urlCreateDTO.getUrlName());
        url.setFullUrl(urlCreateDTO.getFullUrl());
        url.setUrlHealth(urlCreateDTO.getUrlHealth());

        return url;
    }

}
