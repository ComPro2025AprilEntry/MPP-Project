package com.abdelhadi.jobtracker.service;

import com.abdelhadi.jobtracker.dao.JobDAO;
import com.abdelhadi.jobtracker.model.JobApplication;
import org.springframework.stereotype.Service; // Import @Service

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service // Mark as a Spring Service
public class JobService {
    private final JobDAO jobDAO;

    // Constructor for Dependency Injection
    public JobService(JobDAO jobDAO) { // Spring will inject JobDAOImpl
        this.jobDAO = jobDAO;
    }

    // Ensure all methods operate directly on the DAO for the specific userId
    public void addJob(JobApplication job) {
        if (job.getId() == null || job.getId().isEmpty()) {
            job.setId(UUID.randomUUID().toString()); // Assign ID if new
        }
        jobDAO.save(job); // Save individual job
    }

    public void removeJob(String id, String userId) {
        jobDAO.deleteById(id, userId);
    }

    public List<JobApplication> getAllJobs(String userId) {
        return jobDAO.loadAll(userId);
    }

    public Optional<JobApplication> getJobById(String id, String userId) {
        return jobDAO.findById(id, userId);
    }

    public JobApplication updateJob(JobApplication job, String userId) {
        // Ensure the job's userId matches the authenticated userId for security
        job.setUserId(userId);
        jobDAO.save(job); // MERGE INTO handles update if ID exists
        return job;
    }

    // --- New Service Methods for User Stories ---

    // 1. Filter jobs by tech stack
    public List<JobApplication> filterJobsByTechStack(String userId, String techStack) {
        return jobDAO.filterByTechStack(userId, techStack);
    }

    // 2. Sort applications by deadline
    public List<JobApplication> sortJobsByDeadline(String userId) {
        return jobDAO.sortByDeadline(userId);
    }

    // New: Filter jobs by status
    public List<JobApplication> filterJobsByStatus(String userId, String status) {
        return jobDAO.filterByStatus(userId, status);
    }

    // 3. Get grouped stats
    public Map<String, Long> getJobStatusStats(String userId) {
        return jobDAO.getJobStatsByStatus(userId);
    }

    public List<JobApplication> getApplicationsByStatus(Long userId, String status) {
        return jobDAO.findByUserIdAndStatus(userId, status);
    }

    // The saveAll method from previous version is removed as individual save is handled
    // within addJob now, and deleteJob also directly uses DAO.
}