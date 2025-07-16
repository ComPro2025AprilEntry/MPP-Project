package com.abdelhadi.jobtracker.dao;

import com.abdelhadi.jobtracker.model.JobApplication;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobDAOImpl implements JobDAO {
    private static JobDAOImpl instance;
    private final Connection conn;

    private JobDAOImpl() {
        try {
            conn = DriverManager.getConnection("jdbc:h2:./jobtrackerdb", "sa", "");
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS applications (
                        id VARCHAR(255) PRIMARY KEY,
                        company VARCHAR(255),
                        position VARCHAR(255),
                        techStack VARCHAR(1000),
                        appliedDate DATE,
                        deadline DATE,
                        status VARCHAR(100),
                        user_id VARCHAR(255)
                    )
                    """);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to DB", e);
        }
    }

    public static JobDAOImpl getInstance() {
        if (instance == null) instance = new JobDAOImpl();
        return instance;
    }

    @Override
    public void saveAll(List<JobApplication> jobs) {
        String sql = """
            MERGE INTO applications KEY(id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (JobApplication job : jobs) {
                ps.setString(1, job.getId());
                ps.setString(2, job.getCompany());
                ps.setString(3, job.getPosition());
                ps.setString(4, String.join(",", job.getTechStack()));
                ps.setDate(5, Date.valueOf(job.getAppliedDate()));
                ps.setDate(6, Date.valueOf(job.getDeadline()));
                ps.setString(7, job.getStatus());
                ps.setString(8, job.getUserId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save jobs", e);
        }
    }

    @Override
    public List<JobApplication> loadAll() {
        List<JobApplication> jobs = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    jobs.add(new JobApplication(
                            rs.getString("id"),
                            rs.getString("company"),
                            rs.getString("position"),
                            List.of(rs.getString("techStack").split(",")),
                            rs.getDate("appliedDate").toLocalDate(),
                            rs.getDate("deadline").toLocalDate(),
                            rs.getString("status"),
                            rs.getString("user_id")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load jobs", e);
        }
        return jobs;
    }

    @Override
    public List<JobApplication> loadAll(String userId) {
        List<JobApplication> jobs = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    jobs.add(new JobApplication(
                            rs.getString("id"),
                            rs.getString("company"),
                            rs.getString("position"),
                            List.of(rs.getString("techStack").split(",")),
                            rs.getDate("appliedDate").toLocalDate(),
                            rs.getDate("deadline").toLocalDate(),
                            rs.getString("status"),
                            rs.getString("user_id")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load jobs", e);
        }
        return jobs;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM applications WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete job with id: " + id, e);
        }
    }
}

