package com.pavel.dinit.project.controllers;

import com.pavel.dinit.project.dtos.UserCreateDto;
import com.pavel.dinit.project.dtos.UserLoginDto;
import com.pavel.dinit.project.dtos.UserReadDto;
import com.pavel.dinit.project.dtos.UserRegisterDto;
import com.pavel.dinit.project.exceptions.badrequest.ApiBadRequest;
import com.pavel.dinit.project.services.AuthenticationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    // Requesting creation of a new single USER:
    @PostMapping("/register")
    public ResponseEntity<UserCreateDto> register(@RequestBody UserRegisterDto registerDto) {
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
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(user);
    }


    @PostMapping("/login")
    public ResponseEntity<UserReadDto> login(@RequestBody UserLoginDto loginDto) {
        UserReadDto user = authenticationService.login(loginDto);

        // Return the full user information
        return ResponseEntity.ok(user);
    }


}
