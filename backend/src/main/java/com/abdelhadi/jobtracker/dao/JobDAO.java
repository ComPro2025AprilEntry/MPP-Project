package com.abdelhadi.jobtracker.dao;

import com.abdelhadi.jobtracker.model.JobApplication;

import java.util.List;

public interface JobDAO {
    void saveAll(List<JobApplication> jobs);
    List<JobApplication> loadAll();
    List<JobApplication> loadAll(String userId);
    void deleteById(String id);
}

