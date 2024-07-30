package com.pavel.dinit.project.dtos;

import com.pavel.dinit.project.models.UrlHealthHistory;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UrlHealthHistoryDto {

    private Long id;
    private Long urlId;
    private Boolean healthStatus;
    private LocalDateTime timestamp;




    public static UrlHealthHistoryDto fromEntity(UrlHealthHistory history) {
        UrlHealthHistoryDto dto = new UrlHealthHistoryDto();
        dto.setId(history.getId());
        dto.setUrlId(history.getUrlId().getUrlId()); // Assuming URL entity has getUrlId() method
        dto.setHealthStatus(history.getHealthStatus());
        dto.setTimestamp(history.getTimestamp());
        return dto;
    }

}
