package com.pavelDinit.dinitProject.repo;

import com.pavelDinit.dinitProject.model.Urls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlsRepo extends JpaRepository <Urls, Long> {


}
