package com.pavelDinit.dinitProject.dtos;

import lombok.*;
import com.pavelDinit.dinitProject.models.Url;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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

    public static boolean checkUrlValidity(String fullUrl){
        String fullUrlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
        Pattern pattern = Pattern.compile(fullUrlRegex);
        Matcher m = pattern.matcher(fullUrl);

        System.out.println("Url is: " + m.matches());
        return m.matches();
    }

}
