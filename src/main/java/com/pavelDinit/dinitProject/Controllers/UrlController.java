package com.pavelDinit.dinitProject.Controllers;

import com.pavelDinit.dinitProject.dtos.UrlCreationDto;
import com.pavelDinit.dinitProject.dtos.UrlReadingDto;
import com.pavelDinit.dinitProject.models.Url;
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

    @GetMapping
        public List<UrlReadingDto> getUrls(){
        return urlService.getAllUrls();
    }

    // Requesting all the URL'S names:
    @GetMapping("/names")
    public List<Object> getUrlNames() {
        return urlService.getAllUrlNames();
    }


    // Requesting a single URL based on its ID:
    @GetMapping("/{id}")
    public Url getUrlById(@PathVariable Long id) {
        return urlService.getUrlById(id);
    }

    // Requesting a single URL based on its name
    // This is because we don't have id in the dto, so we set it by name
    @GetMapping("/neDela/{urlName}")
    public UrlReadingDto getUrlByName(@PathVariable String urlName) {
        UrlReadingDto urlReadDTO = urlService.getUrlByName(urlName);
        return urlReadDTO;
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


    @PostMapping("/postFromDPO")
    public String createUrlFromDto(@RequestBody UrlCreationDto dto){
        return urlService.createUrlFromDto(dto);
    }
}
