package com.abdelhadi.jobtracker.user.dao;

import com.abdelhadi.jobtracker.user.model.User;

import java.sql.*;
import java.util.UUID;

public class UserDAOImpl implements UserDAO {
    private static UserDAOImpl instance;
    private final Connection conn;

    private UserDAOImpl() {
        try {
            conn = DriverManager.getConnection("jdbc:h2:./jobtrackerdb", "sa", "");
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id VARCHAR(255) PRIMARY KEY,
                        name VARCHAR(255),
                        email VARCHAR(255) UNIQUE,
                        password VARCHAR(255)
                    )
                """);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to DB", e);
        }
    }

    public static UserDAOImpl getInstance() {
        if (instance == null) instance = new UserDAOImpl();
        return instance;
    }

    @Override
    public void register(User user) {
        String sql = "INSERT INTO users (id, name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("User registration failed", e);
        }
    }

    @Override
    public User findByEmail(String email) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email", e);
        }
    }
}

