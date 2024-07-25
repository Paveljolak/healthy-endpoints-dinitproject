package com.pavel.dinit.project.controllers;

import com.pavel.dinit.project.dtos.EditUserDto;
import com.pavel.dinit.project.dtos.UserCreateDto;
import com.pavel.dinit.project.dtos.UserReadDto;
import com.pavel.dinit.project.exceptions.badrequest.TypeMissmatch;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import com.pavel.dinit.project.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {

    private static final String INVALID_USERID = "Invalid User ID: ";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Requesting all the USER'S names:
    @GetMapping
    public List<UserReadDto> getAllUsers() {
        return userService.getAllUsers();
    }


    // Requesting a single USER based on its id:
    @GetMapping("/{id}")
    public ResponseEntity<UserReadDto> getUserById(@PathVariable String id) {
        try {
            Long userId = Long.parseLong(id);
            UserReadDto userReadDto = userService.getUserById(userId);
            return ResponseEntity.ok(userReadDto);
        } catch (NumberFormatException ex) {
            throw new TypeMissmatch(INVALID_USERID + id);
        } catch (MethodArgumentTypeMismatchException ex) {
            throw new TypeMissmatch(INVALID_USERID + ex.getValue());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable String id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new UnauthorizedException("User is not authenticated");
            }

            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            Long userId = Long.parseLong(id);
            return userService.deleteUserById(userId, username);
        } catch (NumberFormatException ex) {
            throw new TypeMissmatch(INVALID_USERID + id);
        } catch (MethodArgumentTypeMismatchException ex) {
            throw new TypeMissmatch(INVALID_USERID + ex.getValue());
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Authentication failed: " + ex.getMessage());
        }
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Void> editUser(
            @PathVariable String id,
            @RequestBody UserReadDto editUserRequest) {
        try {
            Long userId = Long.parseLong(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            userService.editUser(userId, editUserRequest, username);
            return ResponseEntity.noContent().build();
        } catch (NumberFormatException ex) {
            throw new TypeMissmatch(INVALID_USERID + id);
        } catch (MethodArgumentTypeMismatchException ex) {
            throw new TypeMissmatch(INVALID_USERID + ex.getValue());
        }
    }

}