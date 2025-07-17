package com.abdelhadi.jobtracker.service;

import com.abdelhadi.jobtracker.dao.JobDAO; // Import your JobDAO
import com.abdelhadi.jobtracker.model.JobApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JobService {

    private final JobDAO jobDAO; // Inject JobDAO

    @Autowired
    public JobService(JobDAO jobDAO) { // Constructor for injection
        this.jobDAO = jobDAO;
    }

    public Optional<JobApplication> getJobById(String id, String userId) {
        return jobDAO.findById(id, userId);
    }

    public List<JobApplication> getAllJobs(String userId) {
        return jobDAO.loadAll(userId);
    }

    public JobApplication updateJob(JobApplication job, String userId) {
        job.setUserId(userId); // Ensure userId is correctly set
        jobDAO.save(job); // MERGE operation in DAO handles update
        return job;
    }

    public void addJob(JobApplication job) {
        jobDAO.save(job);
    }

    public void removeJob(String id, String userId) {
        jobDAO.deleteById(id, userId);
    }

    public List<JobApplication> filterJobsByTechStack(String userId, String techStack) {
        return jobDAO.filterByTechStack(userId, techStack);
    }

    public List<JobApplication> filterJobsByStatus(String userId, String status) {
        return jobDAO.filterByStatus(userId, status); // ⭐⭐ CALLS YOUR EXISTING DAO METHOD ⭐⭐
    }

    public List<JobApplication> sortJobsByDeadline(String userId) {
        return jobDAO.sortByDeadline(userId);
    }

    public Map<String, Long> getJobStatusStats(String userId) {
        return jobDAO.getJobStatsByStatus(userId);
    }

    public int getTotalApplications(String userId) {
        return jobDAO.calculateTotalApplications(userId);
    }
}