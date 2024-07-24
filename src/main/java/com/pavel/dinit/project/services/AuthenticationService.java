package com.pavel.dinit.project.services;

import com.pavel.dinit.project.dtos.UserCreateDto;
import com.pavel.dinit.project.dtos.UserLoginDto;
import com.pavel.dinit.project.dtos.UserReadDto;
import com.pavel.dinit.project.dtos.UserRegisterDto;
import com.pavel.dinit.project.exceptions.conflict.Conflict;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import com.pavel.dinit.project.models.User;
import com.pavel.dinit.project.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private static final SecureRandom random = new SecureRandom();
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AlertService alertService;

    public AuthenticationService(UserRepo userRepo, PasswordEncoder passwordEncoder, AlertService alertService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.alertService = alertService;
    }


    @Transactional
    public UserCreateDto register(UserRegisterDto registerDto) {
        Optional<User> existingUser = userRepo.findByUsername(registerDto.getUsername());
        if (existingUser.isPresent()) {
            throw new Conflict("Username already exists.");
        }
        Optional<User> existingEmail = userRepo.findByEmail(registerDto.getEmail());
        if (existingEmail.isPresent()) {
            throw new Conflict("Email already exists.");
        }

        // Validate email format
        if (!isValidEmail(registerDto.getEmail())) {
            throw new Conflict("Invalid email format.");
        }

        String verificationCode = generateVerificationCode();

        UserCreateDto createDto = new UserCreateDto();
        createDto.setUsername(registerDto.getUsername());
        createDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        createDto.setEmail(registerDto.getEmail());
        createDto.setRole("USER");
        createDto.setEnabled(true);
        createDto.setVerificationCode(verificationCode);

        User user = UserCreateDto.createDtoToUser(createDto);

        userRepo.save(user);

        alertService.sendEmail(user.getEmail(), "Welcome to Dinit Project", "Your account has been created.");
        logger.info("The email was sent to: " + user.getEmail());

        return UserCreateDto.userCreateDtoFromUser(user);
    }


    private String generateVerificationCode(){
        StringBuilder stringBuilder = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(CODE_CHARACTERS.length());
            stringBuilder.append(CODE_CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    @Transactional(readOnly = true)
    public UserReadDto login(UserLoginDto loginDto) {
        Optional<User> optionalUser = userRepo.findByUsername(loginDto.getUsername());
        if (optionalUser.isEmpty()) {
            throw new UnauthorizedException("Invalid username or password");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        return UserReadDto.readingDtoFromUser(user);
    }
}
