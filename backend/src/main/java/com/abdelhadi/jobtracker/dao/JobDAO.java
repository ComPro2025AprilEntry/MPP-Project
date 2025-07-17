package com.abdelhadi.jobtracker.dao;

import com.abdelhadi.jobtracker.model.JobApplication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JobDAO {
    void initializeTable();
    void save(JobApplication job);
    void deleteById(String id, String userId); // Modified for security
    Optional<JobApplication> findById(String id, String userId); // Modified for security
    List<JobApplication> loadAll(String userId);

    List<JobApplication> filterByTechStack(String userId, String techStack);
    List<JobApplication> sortByDeadline(String userId);
    Map<String, Long> getJobStatsByStatus(String userId);
    List<JobApplication> filterByStatus(String userId, String status);
    int calculateTotalApplications(String userId);
}