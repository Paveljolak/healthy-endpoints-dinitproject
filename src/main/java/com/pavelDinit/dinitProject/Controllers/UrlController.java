package com.pavelDinit.dinitProject.Controllers;

import com.pavelDinit.dinitProject.model.Url;
import com.pavelDinit.dinitProject.service.UrlService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@RestController
@RequestMapping("url")
public class UrlController {


    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // Requesting all the URL'S in a list:
    @GetMapping
    public List<Url> getAllUrls() {
        return urlService.getAll();
    }

    // Requesting all the URL'S names:
    @GetMapping("/names")
    public List<String> getUrlNames() {
        return urlService.getAllUrlNames();
    }

    // Requesting a single URL based on its ID:
    @GetMapping("/{id}")
    public Url getUrlById(@PathVariable Long id) {
        return urlService.getUrlById(id);
    }

    // Requesting a deletion of a URL based on its ID:
    @DeleteMapping("/{id}")
    public String deleteUrlById(@PathVariable Long id) {
        return urlService.deleteById(id);
    }

    // Requesting a deletion of a single URL based on the ID:
    @PostMapping
    public String addUrl(@RequestBody Url url) {
        return urlService.addUrl(url);
    }




}
