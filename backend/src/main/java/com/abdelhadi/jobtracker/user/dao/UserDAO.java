package com.abdelhadi.jobtracker.user.dao;

import com.abdelhadi.jobtracker.user.model.User;

public interface UserDAO {
    void register(User user);
    User findByEmail(String email);
}

