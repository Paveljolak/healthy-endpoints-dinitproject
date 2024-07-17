package com.pavel.dinit.project.services;

import com.pavel.dinit.project.models.Url;
import com.pavel.dinit.project.models.User;
import com.pavel.dinit.project.repo.UrlRepo;
import com.pavel.dinit.project.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessControlService {

    private final UserRepo userRepo;
    private final UrlRepo urlRepo;

    @Autowired
    public AccessControlService(UserRepo userRepo, UrlRepo urlRepo) {
        this.userRepo = userRepo;
        this.urlRepo = urlRepo;
    }

    public boolean isAdmin(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found."));
        return "ADMIN,USER".equalsIgnoreCase(user.getRole());
    }

    public boolean canEdit(Long urlId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found."));

        Optional<Url> optionalUrl = urlRepo.findById(urlId);
        if (optionalUrl.isEmpty()) {
            throw new ResourceNotFoundException("URL with ID " + urlId + " not found.");
        }

        Url url = optionalUrl.get();

        // Check if user is admin or if the user owns the URL
        return isAdmin(username) || user.getId().equals(url.getAddedByUserId().getId());
    }


    // The code is duplicated for now, since we have one for updating one for deleting:
    // So we can easily change which users we want to let to have permissions:
    public boolean canDelete(Long urlId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found."));

        Optional<Url> optionalUrl = urlRepo.findById(urlId);
        if (optionalUrl.isEmpty()) {
            throw new ResourceNotFoundException("URL with ID " + urlId + " not found.");
        }

        Url url = optionalUrl.get();

        // Check if user is admin or if the user owns the URL
        return isAdmin(username) || user.getId().equals(url.getAddedByUserId().getId());
    }
}