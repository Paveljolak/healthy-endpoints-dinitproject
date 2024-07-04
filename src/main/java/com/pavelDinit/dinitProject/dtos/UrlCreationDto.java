package com.pavelDinit.dinitProject.dtos;

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


}

