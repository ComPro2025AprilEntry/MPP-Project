package com.abdelhadi.jobtracker.dao;

import com.abdelhadi.jobtracker.model.JobApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource; // Import DataSource
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Repository // Mark as a Spring Data Repository
public class JobDAOImpl implements JobDAO {

    private final JdbcTemplate jdbcTemplate;

    // Constructor for Dependency Injection
    public JobDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        initializeTable(); // Call initialization method
    }

    private void initializeTable() {
        // Create the applications table if it doesn't exist
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS applications (
                id VARCHAR(255) PRIMARY KEY,
                company VARCHAR(255),
                position VARCHAR(255),
                techStack VARCHAR(1000), -- Storing as comma-separated string
                appliedDate DATE,
                deadline DATE,
                status VARCHAR(100),
                user_id VARCHAR(255)
            )
            """);
    }

    // RowMapper for JobApplication objects
    private final RowMapper<JobApplication> jobApplicationRowMapper = (rs, rowNum) -> {
        try {
            return new JobApplication(
                    rs.getString("id"),
                    rs.getString("company"),
                    rs.getString("position"),
                    List.of(rs.getString("techStack").split(",")), // Convert comma-separated string back to List
                    rs.getDate("appliedDate").toLocalDate(),
                    rs.getDate("deadline").toLocalDate(),
                    rs.getString("status"),
                    rs.getString("user_id")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping JobApplication row", e);
        }
    };

    @Override
    public void save(JobApplication job) { // Changed from saveAll to save single job
        String sql = """
            MERGE INTO applications KEY(id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sql,
                job.getId(),
                job.getCompany(),
                job.getPosition(),
                String.join(",", job.getTechStack()), // Convert List to comma-separated string
                Date.valueOf(job.getAppliedDate()),
                Date.valueOf(job.getDeadline()),
                job.getStatus(),
                job.getUserId()
        );
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM applications WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<JobApplication> loadAll(String userId) {
        String sql = "SELECT * FROM applications WHERE user_id = ?";
        return jdbcTemplate.query(sql, jobApplicationRowMapper, userId);
    }

    // --- New Methods for User Stories ---

    @Override
    public List<JobApplication> filterByTechStack(String userId, String techStack) {
        // Using ILIKE for partial match on comma-separated string.
        // This is a simple solution. For more robust tech stack filtering,
        // you might consider a separate 'job_tech_stack' many-to-many table.
        String sql = "SELECT * FROM applications WHERE user_id = ? AND techStack ILIKE ?";
        return jdbcTemplate.query(sql, jobApplicationRowMapper, userId, "%" + techStack + "%");
    }

    @Override
    public List<JobApplication> sortByDeadline(String userId) {
        String sql = "SELECT * FROM applications WHERE user_id = ? ORDER BY deadline ASC NULLS LAST";
        // NULLS LAST ensures jobs with no deadline are at the end
        return jdbcTemplate.query(sql, jobApplicationRowMapper, userId);
    }

    @Override
    public Map<String, Long> getJobStatsByStatus(String userId) {
        String sql = "SELECT status, COUNT(*) AS count FROM applications WHERE user_id = ? GROUP BY status";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId);

        Map<String, Long> stats = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String status = (String) row.get("status");
            Long count = ((Number) row.get("count")).longValue();
            stats.put(status, count);
        }
        return stats;
    }
}