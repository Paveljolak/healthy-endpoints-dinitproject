package com.pavel.dinit.project.dtos;

import com.pavel.dinit.project.models.Url;
import com.pavel.dinit.project.models.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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

    public UrlCreationDto(String youTube, String url, Object o) {
    }


    public static Url creationToUrlEnt(UrlCreationDto dto, boolean urlHealth, User user){
        Url url = new Url();
        url.setAddedByUserId(user);
        url.setUrlName(dto.getUrlName());
        url.setFullUrl(dto.getFullUrl());
        url.setUrlHealth(urlHealth);
        return url;
    }


    public static UrlCreationDto creationDtoFromUrl(Url url){
        return new UrlCreationDto(
                url.getAddedByUserId() != null ? url.getAddedByUserId().getId() : null,
                url.getUrlName(),
                url.getFullUrl(),
                url.getUrlHealth()
        );
    }


}

