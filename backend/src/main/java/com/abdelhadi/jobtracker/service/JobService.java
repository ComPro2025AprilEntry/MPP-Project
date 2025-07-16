package com.abdelhadi.jobtracker.service;

import com.abdelhadi.jobtracker.dao.JobDAO;
import com.abdelhadi.jobtracker.model.JobApplication;

import java.util.*;
import java.util.stream.Collectors;

public class JobService {
    private final JobDAO jobDAO;
    private List<JobApplication> applications;

    public JobService(JobDAO jobDAO) {
        this.jobDAO = jobDAO;
        this.applications = new ArrayList<>();
//        this.applications = jobDAO.loadAll();
    }

    public void addJob(JobApplication job) {
        applications.add(job);
    }

    public void removeJob(String id) {
        System.out.println(id);
        jobDAO.deleteById(id);
//        applications.removeIf(job -> job.getId().equals(id));
    }

    public List<JobApplication> filterByStatus(String status) {
        return applications.stream()
                .filter(job -> job.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public List<JobApplication> sortByDeadline() {
        return applications.stream()
                .sorted(Comparator.comparing(JobApplication::getDeadline))
                .collect(Collectors.toList());
    }

    public Map<String, Long> groupByStatus() {
        return applications.stream()
                .collect(Collectors.groupingBy(JobApplication::getStatus, Collectors.counting()));
    }

    public List<JobApplication> getAllJobs(String userId) {
        return jobDAO.loadAll(userId);
    }

    public void saveAll() {
        jobDAO.saveAll(applications);
    }
}
