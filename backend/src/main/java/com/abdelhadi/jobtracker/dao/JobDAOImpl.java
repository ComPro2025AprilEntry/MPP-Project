package com.abdelhadi.jobtracker.dao;

import com.abdelhadi.jobtracker.model.JobApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

// import javax.sql.DataSource; // Remove this import as DataSource is no longer directly used in constructor
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository // Mark as a Spring Data Repository
public class JobDAOImpl implements JobDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<JobApplication> jobRowMapper = new RowMapper<JobApplication>() {
        @Override
        public JobApplication mapRow(ResultSet rs, int rowNum) throws SQLException {
            String techStackString = rs.getString("tech_stack");
            List<String> techStackList = (techStackString != null && !techStackString.isEmpty()) ?
                    Arrays.asList(techStackString.split(",")) :
                    List.of(); // Use List.of() for immutable empty list

            return new JobApplication(
                    rs.getString("id"),
                    rs.getString("company"),
                    rs.getString("position"),
                    techStackList,
                    rs.getObject("applied_date", LocalDate.class),
                    rs.getObject("deadline", LocalDate.class),
                    rs.getString("status"),
                    rs.getString("user_id")
            );
        }
    };

    // Constructor for Dependency Injection - MODIFIED
    public JobDAOImpl(JdbcTemplate jdbcTemplate) { // Accept JdbcTemplate directly
        this.jdbcTemplate = jdbcTemplate; // Assign the directly injected JdbcTemplate
        initializeTable(); // Call initialization method (ensure this doesn't cause issues in tests if it tries to execute SQL against a mocked JdbcTemplate)
    }

    public void initializeTable() {
        // Create the applications table if it doesn't exist
        // In a unit test with a mocked JdbcTemplate, this `execute` call would typically be mocked
        // or ignored, as there's no real database to create a table on.
        // If this causes issues, you might need to mock jdbcTemplate.execute(anyString())
        // in your JobDAOImplTest's @BeforeEach setup.
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

    // RowMapper for JobApplication objects (retained for clarity, though jobRowMapper exists)
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
    public void deleteById(String id, String userId) {
        String sql = "DELETE FROM applications WHERE id = ? AND user_id = ?";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public Optional<JobApplication> findById(String id, String userId) {
        String sql = "SELECT id, company, position, tech_stack, applied_date, deadline, status, user_id FROM applications WHERE id = ? AND user_id = ?";
        List<JobApplication> jobs = jdbcTemplate.query(sql, jobRowMapper, id, userId);
        return jobs.stream().findFirst();
    }

    @Override
    public List<JobApplication> loadAll(String userId) {
        String sql = "SELECT * FROM applications WHERE user_id = ?";
        return jdbcTemplate.query(sql, jobApplicationRowMapper, userId);
    }

    // --- New Methods for User Stories ---

    @Override
    public List<JobApplication> filterByTechStack(String userId, String techStack) {
        String sql = "SELECT * FROM applications WHERE user_id = ? AND techStack ILIKE ?";
        return jdbcTemplate.query(sql, jobApplicationRowMapper, userId, "%" + techStack + "%");
    }

    @Override
    public List<JobApplication> sortByDeadline(String userId) {
        String sql = "SELECT * FROM applications WHERE user_id = ? ORDER BY deadline ASC NULLS LAST";
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

    @Override
    public List<JobApplication> filterByStatus(String userId, String status) {
        String sql = "SELECT id, company, position, tech_stack, applied_date, deadline, status, user_id FROM applications WHERE user_id = ? AND status = ?";
        return jdbcTemplate.query(sql, jobRowMapper, userId, status);
    }

    @Override
    public int calculateTotalApplications(String userId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return (count != null) ? count : 0;
    }

    @Override
    public List<JobApplication> findByUserIdAndStatus(Long userId, String status) {
        return List.of();
    }

    @Override
    public List<JobApplication> findByUserIdAndStatus(String userId, String status) {
        String sql = "SELECT id, company, position, tech_stack, applied_date, deadline, status, user_id FROM applications WHERE user_id = ? AND status = ?";
        return jdbcTemplate.query(sql, jobRowMapper, userId, status);
    }
}