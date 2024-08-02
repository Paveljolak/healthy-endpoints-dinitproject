package com.pavel.dinit.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "healthHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlHealthHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_Id", nullable = false)
    private Url urlId;

    @Column(name = "health_status")
    private Boolean healthStatus;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "UrlHealthHistory{" +
                "id=" + id +
                ", url=" + urlId +
                ", health_status=" + healthStatus +
                ", timestamp=" + timestamp +
                '}';
    }
}
