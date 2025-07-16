package com.abdelhadi.jobtracker.user.service;

import com.abdelhadi.jobtracker.user.dao.UserDAO;
import com.abdelhadi.jobtracker.user.model.User;
import org.springframework.stereotype.Service; // Import @Service

import java.util.UUID;

@Service // Mark as a Spring Service
public class UserService {
    private final UserDAO userDAO;

    // Constructor for Dependency Injection
    public UserService(UserDAO userDAO) { // Spring will inject UserDAOImpl
        this.userDAO = userDAO;
    }

    public User register(String name, String email, String password) {
        // In a real app, hash the password here (e.g., using BCrypt) before storing.
        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email, password); // Password is not hashed here for simplicity
        userDAO.register(user);
        return user;
    }

    public User login(String email, String password) {
        User user = userDAO.findByEmail(email);
        // In a real app, compare hashed password using BCrypt.
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}