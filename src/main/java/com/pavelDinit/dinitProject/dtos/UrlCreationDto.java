package com.pavelDinit.dinitProject.dtos;

import com.pavelDinit.dinitProject.models.Url;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UrlCreationDto {

    @NotNull(message = "UserID cannot be empty.")
    private Long addedByUserId;

    @NotNull(message = "URL name cannot be empty.")
    @Size(min = 1, max = 255, message = "URL name length must be between 1 and 255 characters")
    private String urlName;

    @NotNull(message = "The URL field cannot be empty.")
    @Size(min = 1, max = 255, message = "Full URL length must be between 1 and 255 characters")
    @Pattern(regexp = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]", message = "Invalid URL format")
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

