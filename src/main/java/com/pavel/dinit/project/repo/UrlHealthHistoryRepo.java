package com.pavel.dinit.project.repo;

import com.pavel.dinit.project.models.Url;
import com.pavel.dinit.project.models.UrlHealthHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UrlHealthHistoryRepo extends JpaRepository<UrlHealthHistory, Long> {
    List<UrlHealthHistory> findByUrlId(Url url);

    boolean existsByHealthStatusAndTimestamp(boolean healthStatus, LocalDateTime timestamp);
}
