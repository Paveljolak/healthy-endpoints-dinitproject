package com.pavel.dinit.project.controllers;

import com.pavel.dinit.project.dtos.UserCreateDto;
import com.pavel.dinit.project.dtos.UserReadDto;
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
    public String register(@RequestBody UserCreateDto createDto) {

        if (createDto.getUsername() == null || createDto.getUsername().isEmpty()) {
            throw new ApiBadRequest("Username must be specified.");
        }
        if (createDto.getPassword() == null || createDto.getPassword().isEmpty()) {
            throw new ApiBadRequest("Password must be specified.");
        }
        if (createDto.getEmail() == null || createDto.getEmail().isEmpty()) {
            throw new ApiBadRequest("Email must be specified.");
        }

        return authenticationService.register(createDto);
    }


    @PostMapping("/login")
    public String login(@RequestBody UserCreateDto loginDto) {
        authenticationService.login(loginDto);
        return "User is: " + loginDto.getUsername();
    }


}
