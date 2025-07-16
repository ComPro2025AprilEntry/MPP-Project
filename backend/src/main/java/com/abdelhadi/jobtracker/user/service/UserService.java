package com.abdelhadi.jobtracker.user.service;

import com.abdelhadi.jobtracker.user.dao.UserDAO;
import com.abdelhadi.jobtracker.user.model.User;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO dao) {
        this.userDAO = dao;
    }

    public User register(String name, String email, String password) {
        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email, password);
        userDAO.register(user);
        return user;
    }

    public User login(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }
}

