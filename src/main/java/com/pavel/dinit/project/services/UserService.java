package com.pavel.dinit.project.services;

import com.pavel.dinit.project.dtos.UserCreateDto;
import com.pavel.dinit.project.dtos.UserReadDto;
import com.pavel.dinit.project.exceptions.badrequest.TypeMissmatch;
import com.pavel.dinit.project.exceptions.notfound.ResourceNotFound;
import com.pavel.dinit.project.models.User;
import com.pavel.dinit.project.repo.UrlRepo;
import com.pavel.dinit.project.repo.UserRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    private static final String NO_USERS_STORED_ATM = "There are no USERS stored at the moment.";

    private final UserRepo userRepo;


    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    // Function to get all USERS in a list:
    public List<UserReadDto> getAllUsers() {
        List<User> allUsers = userRepo.findAll();
        if(allUsers.isEmpty()) {
            throw new ResourceNotFound(NO_USERS_STORED_ATM);
        }
        return allUsers.stream().map(UserReadDto::readingDtoFromUser).toList();
    }


    public UserReadDto getUserById(Long userId) {
        if(userId == null || userId <= 0) {
            throw new TypeMissmatch("Invalid User ID: " + userId);
        }

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("USER with name " + userId + " not found."));
        return UserReadDto.readingDtoFromUser(user);
    }


    // Function to delete a single URL based on its ID:
    public String deleteUserById(Long userId) {
        if (!userRepo.existsById(Math.toIntExact(userId))) {
            throw new ResourceNotFound("There is no user with id " + userId + ".");
        }
        userRepo.deleteById(userId);
        return "User with id " + userId + " has been deleted";
    }


    // Function to create a single USER:
    public String addUser(UserCreateDto createDto) {

        // Check if the information is valid:
        // will be added later

        User user = UserCreateDto.createDtoToUser(createDto);
        userRepo.save(user);

        return "Created USER with username: " + user.getUsername();

    }
}
