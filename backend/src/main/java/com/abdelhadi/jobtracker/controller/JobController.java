package com.abdelhadi.jobtracker.controller;

import com.abdelhadi.jobtracker.dao.JobDAOImpl;
import com.abdelhadi.jobtracker.model.JobApplication;
import com.abdelhadi.jobtracker.service.JobService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000")

public class JobController {
    private final JobService jobService;

    public JobController() {
        this.jobService = new JobService(JobDAOImpl.getInstance());
    }

    @GetMapping
    public List<JobApplication> getAllJobs(@RequestParam String userId) {
        return jobService.getAllJobs(userId);
    }


    @GetMapping("/status/{status}")
    public List<JobApplication> filterByStatus(@PathVariable String status) {
        return jobService.filterByStatus(status);
    }

    @GetMapping("/sorted")
    public List<JobApplication> sortByDeadline() {
        return jobService.sortByDeadline();
    }

    @GetMapping("/grouped")
    public Map<String, Long> groupByStatus() {
        return jobService.groupByStatus();
    }

    @PostMapping
    public void addJob(@RequestBody JobApplication job) {
        jobService.addJob(job);
        jobService.saveAll();
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable String id) {
        jobService.removeJob(id);
        jobService.saveAll();
    }
}

