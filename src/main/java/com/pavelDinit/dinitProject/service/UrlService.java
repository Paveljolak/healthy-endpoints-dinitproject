package com.pavelDinit.dinitProject.service;

import com.pavelDinit.dinitProject.model.Url;
import com.pavelDinit.dinitProject.repo.UrlRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlService {

    private final UrlRepo urlRepo;


    public UrlService(UrlRepo urlRepo) {
        this.urlRepo = urlRepo;
    }

    // Function to get all the URLS in a list:0
    public List<Url> getAll() {
        List<Url> all = urlRepo.findAll();
        if(all.isEmpty()){
            throw new RuntimeException("There are no urls stored at the moment.");
        }
        return all;
    }

    // Function to get a single URL by its ID:
    public Url getUrlById(Long id) {
        return urlRepo.findById(id).orElseThrow(() -> new RuntimeException("There is no url with id " + id + "."));
    }


    // Function to get all the names of the URLS that are inside the URL list:
    public List<String> getAllUrlNames(){
        List<Url> all = getAll();
        if(all.isEmpty()){
            throw new RuntimeException("There are no urls stored at the moment.");
        }
        return all.stream().map(Url::getUrlname).collect(Collectors.toList());
    }

    // Function to delete a single URL based on its ID:
    public String deleteById(Long id){
        if(urlRepo.existsById(id)){
            urlRepo.deleteById(id);
        }else{
            throw new RuntimeException("There is no url with id " + id + ".");
        }
        return "Url with id " + id + " has been deleted";
    }

    // Function to add a single URL inside the database:
    public String addUrl(Url url){
        urlRepo.save(url);
        return "Created URL with id " + url.getUrlname();
    }



}
