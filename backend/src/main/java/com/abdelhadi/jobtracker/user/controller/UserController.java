package com.abdelhadi.jobtracker.user.controller;

import com.abdelhadi.jobtracker.user.dao.UserDAOImpl;
import com.abdelhadi.jobtracker.user.model.User;
import com.abdelhadi.jobtracker.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService = new UserService(UserDAOImpl.getInstance());

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user.getName(), user.getEmail(), user.getPassword());
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user.getEmail(), user.getPassword());
    }
}

