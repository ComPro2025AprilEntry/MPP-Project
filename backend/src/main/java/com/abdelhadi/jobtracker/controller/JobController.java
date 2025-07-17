package com.abdelhadi.jobtracker.controller;

import com.abdelhadi.jobtracker.model.JobApplication;
import com.abdelhadi.jobtracker.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000") // Ensure this matches your frontend URL

public class JobController {
    private final JobService jobService;

    // Constructor for Dependency Injection
    public JobController(JobService jobService) { // Spring will inject JobService
        this.jobService = jobService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getJobById(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        return jobService.getJobById(id, userId)
                .map(job -> new ResponseEntity<>(job, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<JobApplication> getAllJobs(@RequestParam String userId) {
        return jobService.getAllJobs(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplication> updateJob(@PathVariable String id, @RequestBody JobApplication job, @RequestHeader("X-User-Id") String userId) {
        job.setId(id); // Ensure the ID from the path is used
        return new ResponseEntity<>(jobService.updateJob(job, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JobApplication> addJob(@RequestBody JobApplication job) {
        // Ensure userId is present in the job object when adding/updating
        if (job.getUserId() == null || job.getUserId().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // userId is required
        }
        jobService.addJob(job);
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable String id, @RequestHeader("X-User-Id") String userId) {
        jobService.removeJob(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // --- New Endpoints for User Stories ---

    // Combined endpoint for filtering (by tech stack, status) and sorting
    // Example: GET /api/jobs/filter?userId=user123&techStack=React
    // Example: GET /api/jobs/filter?userId=user123&status=Applied
    // Example: GET /api/jobs/filter?userId=user123&sortBy=deadline
    @GetMapping("/filter")
    public List<JobApplication> filterJobs(
            @RequestParam String userId,
            @RequestParam(required = false) String techStack,
            @RequestParam(required = false) String status) {
        if (techStack != null && !techStack.isEmpty()) {
            return jobService.filterJobsByTechStack(userId, techStack);
        } else if (status != null && !status.isEmpty()) {
            return jobService.filterJobsByStatus(userId, status);
        } else {
            return jobService.getAllJobs(userId); // If no filter is applied, return all jobs
        }
    }

    // Dedicated endpoint for sorting by deadline
    // Example: GET /api/jobs/sorted-by-deadline?userId=user123
    @GetMapping("/sorted-by-deadline")
    public List<JobApplication> sortByDeadline(@RequestParam String userId) {
        return jobService.sortJobsByDeadline(userId);
    }

    // Dedicated endpoint for grouped stats
    // Example: GET /api/jobs/stats?userId=user123
    @GetMapping("/stats")
    public Map<String, Long> getJobStats(@RequestParam String userId) {
        return jobService.getJobStatusStats(userId);
    }
}