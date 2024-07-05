package com.pavelDinit.dinitProject.dtos;

import com.pavelDinit.dinitProject.models.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UrlCreationDto {

    private Long addedByUserId;
    private String urlName;
    private String fullUrl;
    private Boolean urlHealth;


    public static Url creationToUrlEnt(UrlCreationDto dto, boolean urlHealth){
        Url url = new Url();
        url.setAddedByUserId(dto.getAddedByUserId());
        url.setUrlName(dto.getUrlName());
        url.setFullUrl(dto.getFullUrl());
        url.setUrlHealth(urlHealth);
        return url;
    }


    public static UrlCreationDto creationDtoFromUrl(Url url){
        return new UrlCreationDto(
                url.getAddedByUserId(),
                url.getUrlName(),
                url.getFullUrl(),
                url.getUrlHealth()
        );
    }


}

