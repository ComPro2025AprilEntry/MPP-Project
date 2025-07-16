package com.abdelhadi.jobtracker.user.dao;

import com.abdelhadi.jobtracker.user.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource; // Import DataSource

@Repository // Mark as a Spring Data Repository
public class UserDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    // Constructor for Dependency Injection
    // Spring will automatically inject the DataSource (configured by Spring Boot)
    public UserDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        initializeTable(); // Call initialization method
    }

    private void initializeTable() {
        // Create the users table if it doesn't exist
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id VARCHAR(255) PRIMARY KEY,
                name VARCHAR(255),
                email VARCHAR(255) UNIQUE,
                password VARCHAR(255)
            )
        """);
    }

    // RowMapper for User objects to simplify ResultSet mapping
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
    );

    @Override
    public void register(User user) {
        String sql = "INSERT INTO users (id, name, email, password) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM users WHERE email = ?";
        // queryForObject will throw EmptyResultDataAccessException if no row found.
        // We'll catch that and return null, matching previous behavior.
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, email);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }
}