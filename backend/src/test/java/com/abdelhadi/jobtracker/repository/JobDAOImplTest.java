package com.abdelhadi.jobtracker.repository;

import com.abdelhadi.jobtracker.dao.JobDAOImpl;
import com.abdelhadi.jobtracker.model.JobApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class JobDAOImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate; // Mock the JdbcTemplate dependency

    @InjectMocks
    private JobDAOImpl jobDAO; // Inject the mock into JobDAOImpl

    private String testUserId = "testUser123";
    private List<JobApplication> allJobs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Mock the initializeTable() call to prevent it from interfering with other mocks
        // as it calls jdbcTemplate.execute() in the JobDAOImpl constructor.
        doNothing().when(jdbcTemplate).execute(anyString());


        // Prepare a set of dummy JobApplication data
        allJobs = new ArrayList<>();
        allJobs.add(new JobApplication("job1", "Google", "SWE", Arrays.asList("Java", "Spring"),
                LocalDate.of(2023, 1, 15), LocalDate.of(2023, 3, 1), "Applied", testUserId));
        allJobs.add(new JobApplication("job2", "Microsoft", "Dev", Arrays.asList("C#", ".NET"),
                LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 28), "Interviewing", testUserId));
        allJobs.add(new JobApplication("job3", "Apple", "QA", Arrays.asList("Python", "Selenium"),
                LocalDate.of(2023, 3, 1), LocalDate.of(2023, 4, 15), "Rejected", testUserId));
        allJobs.add(new JobApplication("job4", "Amazon", "Data Sci", Arrays.asList("Python", "ML"),
                LocalDate.of(2023, 3, 5), null, "Applied", testUserId)); // Job with null deadline
        allJobs.add(new JobApplication("job5", "Netflix", "Frontend", Arrays.asList("React", "JS"),
                LocalDate.of(2023, 4, 1), LocalDate.of(2023, 2, 15), "Applied", testUserId)); // Past deadline
        allJobs.add(new JobApplication("job6", "Google", "UX Designer", Arrays.asList("Figma"),
                LocalDate.of(2023, 5, 1), LocalDate.of(2023, 6, 1), "Interviewing", testUserId));
    }

    // Removed the problematic mockJdbcTemplateQueryForAllJobs helper.
    // Mocks will be set up directly in each test method for precision.

    @Test
    void filterByStatus_shouldReturnCorrectJobsForStatus() {
        String targetStatus = "Applied";
        List<JobApplication> expectedJobs = allJobs.stream()
                .filter(job -> job.getStatus().equals(targetStatus))
                .collect(Collectors.toList());

        // Correctly mock the query method called by filterByStatus
        when(jdbcTemplate.query(
                eq("SELECT id, company, position, tech_stack, applied_date, deadline, status, user_id FROM applications WHERE user_id = ? AND status = ?"),
                any(RowMapper.class), // Match the RowMapper instance
                eq(testUserId),
                eq(targetStatus)))
                .thenReturn(expectedJobs);

        List<JobApplication> result = jobDAO.filterByStatus(testUserId, targetStatus);

        assertNotNull(result);
        assertEquals(expectedJobs.size(), result.size());
        assertTrue(result.containsAll(expectedJobs));
        verify(jdbcTemplate).query(
                eq("SELECT id, company, position, tech_stack, applied_date, deadline, status, user_id FROM applications WHERE user_id = ? AND status = ?"),
                any(RowMapper.class), // Match the RowMapper instance for verification
                eq(testUserId),
                eq(targetStatus));
    }

    @Test
    void filterByStatus_shouldReturnEmptyListForNonMatchingStatus() {
        String targetStatus = "Offer"; // No jobs with this status in dummy data
        List<JobApplication> expectedJobs = Collections.emptyList();

        // Correctly mock the query method
        when(jdbcTemplate.query(
                eq("SELECT id, company, position, tech_stack, applied_date, deadline, status, user_id FROM applications WHERE user_id = ? AND status = ?"),
                any(RowMapper.class), // Match the RowMapper instance
                eq(testUserId),
                eq(targetStatus)))
                .thenReturn(expectedJobs);

        List<JobApplication> result = jobDAO.filterByStatus(testUserId, targetStatus);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jdbcTemplate).query(
                eq("SELECT id, company, position, tech_stack, applied_date, deadline, status, user_id FROM applications WHERE user_id = ? AND status = ?"),
                any(RowMapper.class), // Match the RowMapper instance for verification
                eq(testUserId),
                eq(targetStatus));
    }

    @Test
    void sortByDeadline_shouldReturnJobsSortedAscendingByDeadline() {
        // Manually sort expected results to compare and for mocking
        List<JobApplication> expectedSortedJobs = allJobs.stream()
                .sorted(Comparator.comparing(JobApplication::getDeadline,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        // Correctly mock the query method called by sortByDeadline
        // The mock should return the list as if it was sorted by the DB
        when(jdbcTemplate.query(
                eq("SELECT * FROM applications WHERE user_id = ? ORDER BY deadline ASC NULLS LAST"),
                any(RowMapper.class), // This will match jobApplicationRowMapper
                eq(testUserId)))
                .thenReturn(expectedSortedJobs); // <-- CHANGE HERE: Return the pre-sorted list

        List<JobApplication> result = jobDAO.sortByDeadline(testUserId);

        assertNotNull(result);
        assertEquals(allJobs.size(), result.size());

        assertEquals(expectedSortedJobs, result, "Jobs should be sorted ascending by deadline, with nulls last.");

        // Verify the correct SQL query for sorting
        verify(jdbcTemplate).query(
                eq("SELECT * FROM applications WHERE user_id = ? ORDER BY deadline ASC NULLS LAST"),
                any(RowMapper.class), // Match the RowMapper instance for verification
                eq(testUserId));
    }

    @Test
    void sortByDeadline_shouldHandleEmptyList() {
        // Correctly mock the query method for an empty result
        when(jdbcTemplate.query(
                eq("SELECT * FROM applications WHERE user_id = ? ORDER BY deadline ASC NULLS LAST"),
                any(RowMapper.class),
                eq(testUserId)))
                .thenReturn(Collections.emptyList());

        List<JobApplication> result = jobDAO.sortByDeadline(testUserId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jdbcTemplate).query(
                eq("SELECT * FROM applications WHERE user_id = ? ORDER BY deadline ASC NULLS LAST"),
                any(RowMapper.class),
                eq(testUserId));
    }

    @Test
    void groupByStatus_shouldReturnCorrectCountsPerStatus() {
        // Prepare mock return for queryForList
        List<Map<String, Object>> mockRows = new ArrayList<>();
        mockRows.add(Map.of("status", "Applied", "count", 3L));
        mockRows.add(Map.of("status", "Interviewing", "count", 2L));
        mockRows.add(Map.of("status", "Rejected", "count", 1L));

        // Correctly mock the queryForList method
        when(jdbcTemplate.queryForList(
                eq("SELECT status, COUNT(*) AS count FROM applications WHERE user_id = ? GROUP BY status"),
                eq(testUserId))) // queryForList takes varargs for parameters
                .thenReturn(mockRows);

        Map<String, Long> expectedCounts = allJobs.stream()
                .collect(Collectors.groupingBy(JobApplication::getStatus, Collectors.counting()));

        Map<String, Long> result = jobDAO.getJobStatsByStatus(testUserId); // Assuming method name is getJobStatsByStatus

        assertNotNull(result);
        assertEquals(expectedCounts.size(), result.size());
        assertEquals(expectedCounts.get("Applied"), result.get("Applied"));
        assertEquals(expectedCounts.get("Interviewing"), result.get("Interviewing"));
        assertEquals(expectedCounts.get("Rejected"), result.get("Rejected"));

        verify(jdbcTemplate).queryForList(
                eq("SELECT status, COUNT(*) AS count FROM applications WHERE user_id = ? GROUP BY status"),
                eq(testUserId)); // Verify the correct SQL query for grouping
    }

    @Test
    void groupByStatus_shouldReturnEmptyMapForNoJobs() {
        // Correctly mock the queryForList method for an empty result
        when(jdbcTemplate.queryForList(
                eq("SELECT status, COUNT(*) AS count FROM applications WHERE user_id = ? GROUP BY status"),
                eq(testUserId)))
                .thenReturn(Collections.emptyList());

        Map<String, Long> result = jobDAO.getJobStatsByStatus(testUserId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jdbcTemplate).queryForList(
                eq("SELECT status, COUNT(*) AS count FROM applications WHERE user_id = ? GROUP BY status"),
                eq(testUserId));
    }

    @Test
    void calculateTotalApplications_shouldReturnCorrectCount() {
        Integer expectedCount = allJobs.size(); // Total number of jobs

        // Correctly mock the queryForObject method
        when(jdbcTemplate.queryForObject(
                eq("SELECT COUNT(*) FROM applications WHERE user_id = ?"),
                eq(Integer.class),
                eq(testUserId))) // queryForObject takes varargs for parameters
                .thenReturn(expectedCount);

        int result = jobDAO.calculateTotalApplications(testUserId);

        assertEquals(expectedCount.intValue(), result);
        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM applications WHERE user_id = ?"),
                eq(Integer.class),
                eq(testUserId));
    }

    @Test
    void calculateTotalApplications_shouldReturnZeroForNoJobs() {
        Integer expectedCount = 0;

        // Correctly mock the queryForObject method for zero count
        when(jdbcTemplate.queryForObject(
                eq("SELECT COUNT(*) FROM applications WHERE user_id = ?"),
                eq(Integer.class),
                eq(testUserId)))
                .thenReturn(expectedCount);

        int result = jobDAO.calculateTotalApplications(testUserId);

        assertEquals(0, result);
        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM applications WHERE user_id = ?"),
                eq(Integer.class),
                eq(testUserId));
    }
}