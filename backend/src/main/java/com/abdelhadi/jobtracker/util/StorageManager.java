package com.abdelhadi.jobtracker.util;

import com.abdelhadi.jobtracker.model.JobApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // Thread-safe list

// This class demonstrates the Singleton pattern using an enum,
// providing a concise, thread-safe, and serialization-safe approach
// for managing file-based persistence of JobApplications.
public enum StorageManager {
    // The single instance of our StorageManager
    INSTANCE;

    private final File storageFile;
    private final ObjectMapper objectMapper;
    private final List<JobApplication> inMemoryJobs; // Simple in-memory cache

    // The constructor for the enum is implicitly private
    StorageManager() {
        this.storageFile = new File("job_applications.json"); // Name of your storage file
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // For LocalDate serialization
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON

        this.inMemoryJobs = new CopyOnWriteArrayList<>(); // Thread-safe list for internal use

        // Load existing data on initialization
        loadJobsFromFile();
        System.out.println("StorageManager (Enum): Initializing and loading jobs from " + storageFile.getName());
    }

    // --- Persistence Methods ---

    // Simulates saving all current jobs to the file
    public void saveJobs(List<JobApplication> jobs) {
        // Update in-memory cache (for this simple example)
        this.inMemoryJobs.clear();
        this.inMemoryJobs.addAll(jobs);

        try {
            objectMapper.writeValue(storageFile, jobs);
            System.out.println("StorageManager (Enum): Saved " + jobs.size() + " jobs to " + storageFile.getName());
        } catch (IOException e) {
            System.err.println("StorageManager (Enum): Failed to save jobs: " + e.getMessage());
        }
    }

    // Simulates loading all jobs from the file
    public List<JobApplication> loadJobs() {
        if (inMemoryJobs.isEmpty() && storageFile.exists() && storageFile.length() > 0) {
            loadJobsFromFile(); // Ensure in-memory is populated if it's empty but file exists
        }
        System.out.println("StorageManager (Enum): Returning " + inMemoryJobs.size() + " jobs from memory/file.");
        return new ArrayList<>(inMemoryJobs); // Return a copy to prevent external modification
    }

    private void loadJobsFromFile() {
        if (storageFile.exists() && storageFile.length() > 0) {
            try {
                JobApplication[] loadedArray = objectMapper.readValue(storageFile, JobApplication[].class);
                this.inMemoryJobs.clear();
                this.inMemoryJobs.addAll(Arrays.asList(loadedArray));
            } catch (IOException e) {
                System.err.println("StorageManager (Enum): Failed to load jobs from file: " + e.getMessage());
            }
        }
    }

    // --- Example Usage Methods (to be called by other services/components) ---

    // Example: Add a single job and save
    public void addJob(JobApplication job) {
        this.inMemoryJobs.add(job);
        saveJobs(this.inMemoryJobs); // Save the updated list
    }

    // Example: Delete a job and save
    public void deleteJob(String jobId) {
        this.inMemoryJobs.removeIf(job -> job.getId().equals(jobId));
        saveJobs(this.inMemoryJobs); // Save the updated list
    }
}