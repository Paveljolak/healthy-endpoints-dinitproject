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

    private static final String USER_NOT_FOUND_MSG = "User with username %s not found.";
    private static final String USER_ID_NO_FOUND_MSG = "User with id %d not found.";

    private final UserRepo userRepo;
    private final UrlRepo urlRepo;

    @Autowired
    public AccessControlService(UserRepo userRepo, UrlRepo urlRepo) {
        this.userRepo = userRepo;
        this.urlRepo = urlRepo;
    }

    public boolean isAdmin(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
        return "ADMIN,USER".equalsIgnoreCase(user.getRole());
    }

    public boolean canAlter(Long urlId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));

        Optional<Url> optionalUrl = urlRepo.findById(urlId);
        if (optionalUrl.isEmpty()) {
            throw new ResourceNotFoundException("URL with ID " + urlId + " not found.");
        }

        Url url = optionalUrl.get();

        // Check if user is admin or if the user owns the URL
        return isAdmin(username) || user.getId().equals(url.getAddedByUserId().getId());
    }


    public boolean canDeleteUser(Long userId, String requestingUsername) {
        User userToDelete = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_ID_NO_FOUND_MSG, userId)));

        return isAdmin(requestingUsername) || requestingUsername.equals(userToDelete.getUsername());
    }

    public boolean canEditUser(Long userIdToEdit, String requestingUsername) {
        // Check if the user requesting the edit is an admin
        if (isAdmin(requestingUsername)) {
            return true;
        }

        // Find the user to be edited
        User userToEdit = userRepo.findById(userIdToEdit)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_ID_NO_FOUND_MSG, userIdToEdit)));

        // Check if the user requesting the edit is the same as the user to be edited
        return requestingUsername.equals(userToEdit.getUsername());
    }


}