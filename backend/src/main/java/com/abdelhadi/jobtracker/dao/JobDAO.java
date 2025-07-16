package com.abdelhadi.jobtracker.dao;

import com.abdelhadi.jobtracker.model.JobApplication;

import java.util.List;
import java.util.Map;

public interface JobDAO {
    void save(JobApplication job); // Changed from saveAll to save single
    void deleteById(String id);
    List<JobApplication> loadAll(String userId);

    // New methods for user stories
    List<JobApplication> filterByTechStack(String userId, String techStack);
    List<JobApplication> sortByDeadline(String userId);
    Map<String, Long> getJobStatsByStatus(String userId);
}