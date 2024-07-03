package com.pavelDinit.dinitProject.repo;

import com.pavelDinit.dinitProject.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepo extends JpaRepository <Url, Long> {


}
