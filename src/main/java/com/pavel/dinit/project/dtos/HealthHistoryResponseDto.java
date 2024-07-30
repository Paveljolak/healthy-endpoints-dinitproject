package com.pavel.dinit.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthHistoryResponseDto {
    private Long urlId;
    private Boolean healthStatus;
    private String timestamp;
    public void setMessage(String s) {
    }
}
