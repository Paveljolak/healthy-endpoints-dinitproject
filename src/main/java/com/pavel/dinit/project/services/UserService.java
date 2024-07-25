package com.pavel.dinit.project.services;

import com.pavel.dinit.project.dtos.EditUserDto;
import com.pavel.dinit.project.dtos.UserCreateDto;
import com.pavel.dinit.project.dtos.UserReadDto;
import com.pavel.dinit.project.exceptions.badrequest.TypeMissmatch;
import com.pavel.dinit.project.exceptions.conflict.Conflict;
import com.pavel.dinit.project.exceptions.notfound.ResourceNotFound;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import com.pavel.dinit.project.models.User;
import com.pavel.dinit.project.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String NO_USERS_STORED_ATM = "There are no USERS stored at the moment.";

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final AccessControlService accessControlService;



    public UserService(UserRepo userRepo, AlertService alertService, AccessControlService accessControlService, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
        this.userRepo = userRepo;
        this.accessControlService = accessControlService;
        this.passwordEncoder = passwordEncoder1;
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
    public ResponseEntity<Map<String, String>> deleteUserById(Long userId, String username) {

        if (!accessControlService.canDeleteUser(userId, username)) {
            throw new UnauthorizedException("Unauthorized.");
        }

        validateUserId(userId);

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("There is no user with id " + userId + "."));

        checkUserEnabled(user);
        userRepo.deleteById(userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User with id " + userId + " has been deleted");

        return new ResponseEntity<>(response, HttpStatus.OK);
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


    public void editUser(Long id, UserReadDto updateUserRequest, String username) {
        if (!accessControlService.canEditUser(id, username)) {
            throw new UnauthorizedException("You are not authorized to edit this user.");
        }

        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (updateUserRequest.getUsername() != null && !updateUserRequest.getUsername().trim().isEmpty()) {
                Optional<User> existingUserWithUsername = userRepo.findByUsername(updateUserRequest.getUsername());
                if (existingUserWithUsername.isPresent() && !existingUserWithUsername.get().getId().equals(id)) {
                    throw new Conflict("Username already exists.");
                }
                user.setUsername(updateUserRequest.getUsername());
            }

            if (updateUserRequest.getEmail() != null && !updateUserRequest.getEmail().trim().isEmpty()) {
                Optional<User> existingUserWithEmail = userRepo.findByEmail(updateUserRequest.getEmail());
                if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(id)) {
                    throw new Conflict("Email already exists.");
                }
                user.setEmail(updateUserRequest.getEmail());
            }

            if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
            }

            if (updateUserRequest.getRole() != null) {
                user.setRole(updateUserRequest.getRole());
            }

            if (updateUserRequest.getEnabled() != null) {
                user.setEnabled(updateUserRequest.getEnabled());
            }

            if (updateUserRequest.getVerificationCode() != null && !updateUserRequest.getVerificationCode().trim().isEmpty()) {
                user.setVerificationCode(updateUserRequest.getVerificationCode());
            }

            userRepo.save(user);
        } else {
            throw new ResourceNotFound("User with id " + id + " not found.");
        }
    }





}