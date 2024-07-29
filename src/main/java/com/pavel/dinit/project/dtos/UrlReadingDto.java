package com.pavel.dinit.project.dtos;

import com.pavel.dinit.project.models.Url;
import com.pavel.dinit.project.models.User;
import lombok.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UrlReadingDto {
    private static final Logger logger = Logger.getLogger(UrlReadingDto.class.getName());

    private Long urlId;
    private Long addedByUserId;
    private String urlName;
    private String fullUrl;
    private Boolean urlHealth;
    private String lastChecked;


    public static UrlReadingDto readingDtoFromUrl(Url url){
        return new UrlReadingDto(
                url.getUrlId(),
                url.getAddedByUserId() != null ? url.getAddedByUserId().getId() : null,
                url.getUrlName(),
                url.getFullUrl(),
                url.getUrlHealth(),
                url.getLastChecked()

        );
    }



    public static Url readingToUrlEnt(UrlReadingDto dto, User user){
        Url url = new Url();
        url.setUrlId(dto.getUrlId());
        url.setAddedByUserId(user);
        url.setUrlName(dto.getUrlName());
        url.setFullUrl(dto.getFullUrl());
        url.setUrlHealth(dto.getUrlHealth());
        url.setLastChecked(dto.getLastChecked());
        return url;
    }

    public static boolean checkUrlValidity(String fullUrl) {
        // Regex pattern to validate URLs
        String fullUrlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
        Pattern pattern = Pattern.compile(fullUrlRegex);
        Matcher matcher = pattern.matcher(fullUrl);

        boolean isValid = matcher.matches();

        // Log the result of URL validation
        if (logger.isLoggable(Level.INFO)) {
            logger.info("URL validation result for '" + fullUrl + "' is: " + isValid);
        }

        // Return true if the URL is valid, false otherwise
        return isValid;
    }
}