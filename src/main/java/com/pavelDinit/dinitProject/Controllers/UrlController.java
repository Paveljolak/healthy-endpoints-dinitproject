package com.pavelDinit.dinitProject.Controllers;

import com.pavelDinit.dinitProject.model.Urls;
import com.pavelDinit.dinitProject.repo.UrlsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("url")
public class UrlController {


    private final UrlsRepo urlsRepo;

    public UrlController(UrlsRepo urlsRepo){
        this.urlsRepo = urlsRepo;
    }


    @PostMapping
    public String addUrl(@RequestBody Urls urls){
        urlsRepo.save(urls);
        return "Created URL: " + urls.getUrlname();
    }

    @GetMapping("/allUrls")
    public List<Urls> GetAll(){
        List<Urls> all = urlsRepo.findAll();
        return all;
    }

    @GetMapping("/allUrlNames")
    public List<String> getAllUrlNames(){
        List<Urls> all = urlsRepo.findAll();
        return all.stream().map(Urls::getUrlname).collect(Collectors.toList());
    }
}
