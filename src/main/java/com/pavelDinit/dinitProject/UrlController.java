package com.pavelDinit.dinitProject;

import com.pavelDinit.dinitProject.model.Urls;
import com.pavelDinit.dinitProject.repo.UrlsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    @Autowired
    UrlsRepo repo;

    @PostMapping("/addUrl")
    public void addUrl(@RequestBody Urls urls){
        repo.save(urls);
    }
}
