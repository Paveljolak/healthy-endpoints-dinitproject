package com.pavel.dinit.project.controllers;

import com.pavel.dinit.project.dtos.UserCreateDto;
import com.pavel.dinit.project.dtos.UserLoginDto;
import com.pavel.dinit.project.dtos.UserReadDto;
import com.pavel.dinit.project.dtos.UserRegisterDto;
import com.pavel.dinit.project.exceptions.badrequest.ApiBadRequest;
import com.pavel.dinit.project.exceptions.conflict.Conflict;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import com.pavel.dinit.project.models.User;
import com.pavel.dinit.project.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.remote.JMXAuthenticator;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    // Requesting creation of a new single USER:
    @PostMapping("/register")
    public String register(@RequestBody UserRegisterDto registerDto) {
        if (registerDto.getUsername() == null || registerDto.getUsername().isEmpty()) {
            throw new ApiBadRequest("Username must be specified.");
        }
        if (registerDto.getPassword() == null || registerDto.getPassword().isEmpty()) {
            throw new ApiBadRequest("Password must be specified.");
        }
        if (registerDto.getEmail() == null || registerDto.getEmail().isEmpty()) {
            throw new ApiBadRequest("Email must be specified.");
        }

        UserCreateDto user = authenticationService.register(registerDto);

        return "Username: " + user.getUsername() +
                ", Password: " + user.getPassword() +
                ", Email: " + user.getEmail() +
                ", Enabled: " + user.isEnabled() +
                ", Verification Code: " + user.getVerificationCode();

    }


    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto loginDto) {
        UserReadDto user = authenticationService.login(loginDto);
        return "Username: " + user.getUsername() +
                ", Password: " + user.getPassword() +
                ", Email: " + user.getEmail() +
                ", Enabled: " + user.isEnabled() +
                ", Verification Code: " + user.getVerificationCode();
    }


}
