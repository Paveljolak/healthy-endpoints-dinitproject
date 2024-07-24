package com.pavel.dinit.project.services;

import com.pavel.dinit.project.dtos.UserReadDto;
import com.pavel.dinit.project.exceptions.badrequest.TypeMissmatch;
import com.pavel.dinit.project.exceptions.notfound.ResourceNotFound;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import com.pavel.dinit.project.models.User;
import com.pavel.dinit.project.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String NO_USERS_STORED_ATM = "There are no USERS stored at the moment.";

    private final UserRepo userRepo;


    private final AccessControlService accessControlService;



    public UserService(UserRepo userRepo, AlertService alertService, AccessControlService accessControlService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.accessControlService = accessControlService;
    }


    // Function to get all USERS in a list:
    public List<UserReadDto> getAllUsers() {
        List<User> allUsers = userRepo.findAll();
        if (allUsers.isEmpty()) {
            throw new ResourceNotFound(NO_USERS_STORED_ATM);
        }
        return allUsers.stream().map(UserReadDto::readingDtoFromUser).toList();
    }


    public UserReadDto getUserById(Long userId) {
        validateUserId(userId);

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("USER with name " + userId + " not found."));

        return UserReadDto.readingDtoFromUser(user);
    }


    // Function to delete a single URL based on its ID:
    public String deleteUserById(Long userId, String username) {

        if (!accessControlService.canDeleteUser(userId, username)) {
            throw new UnauthorizedException("Unauthorized.");
        }

        validateUserId(userId);

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("There is no user with id " + userId + "."));

        checkUserEnabled(user);
        userRepo.deleteById(userId);

        return "User with id " + userId + " has been deleted";
    }




    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new TypeMissmatch("Invalid User ID: " + userId);
        }
    }

    private void checkUserEnabled(User user) {
        if (!user.isEnabled()) {
            throw new UnauthorizedException("User account is not activated.");
        }
    }




}
