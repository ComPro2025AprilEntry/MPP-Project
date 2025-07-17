package com.abdelhadi.jobtracker;

import com.abdelhadi.jobtracker.model.JobApplication;
import com.abdelhadi.jobtracker.util.StorageManager; // Import the new class
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

// For demonstration, you might need to temporarily import JobApplication
// import com.abdelhadi.jobtracker.model.JobApplication;
// import java.time.LocalDate;
// import java.util.Arrays;
// import java.util.UUID;


@SpringBootApplication
public class JobTrackerApp {
    public static void main(String[] args) {
        SpringApplication.run(JobTrackerApp.class, args);

        // --- Explicit Singleton StorageManager Demonstration ---
        System.out.println("\n--- Explicit Singleton StorageManager Demonstration ---");

        StorageManager manager1 = StorageManager.getInstance();
        StorageManager manager2 = StorageManager.getInstance();

        System.out.println("Are manager1 and manager2 the same instance? " + (manager1 == manager2)); // Should be true

        // Example: Load jobs (might be empty initially)
        List<JobApplication> loadedJobs = manager1.loadJobs();
        System.out.println("Jobs loaded via manager1: " + loadedJobs.size());

        // Example: Add a dummy job and save using manager2
        // If you want to test adding, uncomment the imports above
        // JobApplication newJob = new JobApplication(
        //     UUID.randomUUID().toString(), // id
        //     "DemoCo", // company
        //     "Software Engineer", // position
        //     Arrays.asList("Java", "Spring"), // techStack
        //     LocalDate.now(), // appliedDate
        //     LocalDate.now().plusWeeks(2), // deadline
        //     "Applied", // status
        //     "dummyUserId" // userId (ensure this matches existing user if you link)
        // );
        // manager2.addJob(newJob);
        // System.out.println("Added a dummy job via manager2.");

        // // Verify state via manager1
        // loadedJobs = manager1.loadJobs();
        // System.out.println("Jobs loaded via manager1 after adding: " + loadedJobs.size());


        System.out.println("--- End StorageManager Demonstration ---\n");
    }
}