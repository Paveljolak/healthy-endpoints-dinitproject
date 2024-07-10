package com.pavel.dinit.project.repo;

import com.pavel.dinit.project.models.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UrlRepo extends JpaRepository <Url, Long> {

    Optional<Url> findByUrlId(Long urlID);
    Optional<Url> findByFullUrl(String fullUrl);

    @Transactional
    void deleteByUrlId(Long urlId);

}

