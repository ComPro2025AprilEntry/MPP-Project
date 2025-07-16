package com.abdelhadi.jobtracker.user.controller;

import com.abdelhadi.jobtracker.user.model.User;
import com.abdelhadi.jobtracker.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // Ensure this matches your frontend URL

public class UserController {
    private final UserService userService;

    // Constructor for Dependency Injection
    public UserController(UserService userService) { // Spring will inject UserService
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user.getName(), user.getEmail(), user.getPassword());
        if (registeredUser != null) {
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        }
        // In a real app, handle specific errors like email already exists
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        if (loggedInUser != null) {
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}