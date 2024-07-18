package com.pavel.dinit.project.services;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String NO_USERS_STORED_ATM = "There are no USERS stored at the moment.";

    private final UserRepo userRepo;

    private final AlertService alertService;

    private final AccessControlService accessControlService;

    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom random = new SecureRandom();
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public UserService(UserRepo userRepo, AlertService alertService, AccessControlService accessControlService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.alertService = alertService;
        this.accessControlService = accessControlService;
        this.passwordEncoder = passwordEncoder;
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
        validateUserId(userId);

        if (!accessControlService.canDeleteUser(userId, username)) {
            throw new UnauthorizedException("Unauthorized.");
        }

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("There is no user with id " + userId + "."));

        checkUserEnabled(user);
        userRepo.deleteById(userId);

        return "User with id " + userId + " has been deleted";
    }


    // Function to create a single USER:
    @Transactional
    public String register(UserCreateDto createDto) {
        Optional<User> existingUser = userRepo.findByUsername(createDto.getUsername());
        if (existingUser.isPresent()) {
            throw new Conflict("Username already exists.");
        }
        Optional<User> existingEmail = userRepo.findByEmail(createDto.getEmail());
        if (existingEmail.isPresent()) {
            throw new Conflict("Email already exists.");
        }

        String verificationCode = generateVerificationCode();

        User user = UserCreateDto.createDtoToUser(createDto);
        user.setEnabled(true);
        user.setVerificationCode(verificationCode);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        alertService.sendEmail(user.getEmail(), "Welcome to Dinit Project", "Your account has been created.");
        logger.info("The email was sent to: " + user.getEmail());

        return "Created USER with username: " + user.getUsername();
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

    private String generateVerificationCode(){
        StringBuilder stringBuilder = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(CODE_CHARACTERS.length());
            stringBuilder.append(CODE_CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }


}
