package com.pavelDinit.dinitProject.repo;

import com.pavelDinit.dinitProject.models.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepo extends JpaRepository <Url, Long> {

    Optional<Url> findByUrlName(String urlName);

    }

