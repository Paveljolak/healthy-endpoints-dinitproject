package com.pavel.dinit.project.controllers;


import com.pavel.dinit.project.dtos.UserCreateDto;
import com.pavel.dinit.project.dtos.UserReadDto;
import com.pavel.dinit.project.exceptions.badrequest.TypeMissmatch;
import com.pavel.dinit.project.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

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


    // Requesting a deletion of a single USER based on its ID:
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable String id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            Long userId = Long.parseLong(id);
            return userService.deleteUserById(userId, username);
        } catch (NumberFormatException ex) {
            throw new TypeMissmatch(INVALID_USERID + id);
        } catch (MethodArgumentTypeMismatchException ex) {
            throw new TypeMissmatch(INVALID_USERID + ex.getValue());
        }
    }


    // Requesting creation of a new single USER:
    @PostMapping
    public String addUser(@RequestBody UserCreateDto createDto) {
        return userService.addUser(createDto);
    }

}
