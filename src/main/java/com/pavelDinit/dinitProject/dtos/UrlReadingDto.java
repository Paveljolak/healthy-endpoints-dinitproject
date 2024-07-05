package com.pavelDinit.dinitProject.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.pavelDinit.dinitProject.models.Url;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UrlReadingDto {

    private Long urlId;
    private Long addedByUserId;
    private String urlName;
    private String fullUrl;
    private Boolean urlHealth;
    private String lastChecked;


    public static UrlReadingDto readingDtoFromUrl(Url url){
        return new UrlReadingDto(
                url.getUrlId(),
                url.getAddedByUserId(),
                url.getUrlName(),
                url.getFullUrl(),
                url.getUrlHealth(),
                url.getLastChecked()
        );
    }



    public static Url readingToUrlEnt(UrlReadingDto dto){
        Url url = new Url();
        url.setUrlId(dto.getUrlId());
        url.setAddedByUserId(dto.getAddedByUserId());
        url.setUrlName(dto.getUrlName());
        url.setFullUrl(dto.getFullUrl());
        url.setUrlHealth(dto.getUrlHealth());
        url.setLastChecked(dto.getLastChecked());
        return url;
    }
}
