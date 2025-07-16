package com.abdelhadi.jobtracker;

import com.abdelhadi.jobtracker.util.ConfigurationManager; 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobTrackerApp {
    public static void main(String[] args) {
        SpringApplication.run(JobTrackerApp.class, args);

        // Demonstrate explicit Singleton usage
        ConfigurationManager config1 = ConfigurationManager.getInstance();
        ConfigurationManager config2 = ConfigurationManager.getInstance();

        System.out.println("\n--- Explicit Singleton Demonstration ---");
        System.out.println("Are config1 and config2 the same instance? " + (config1 == config2)); // Should be true
        System.out.println("App Version from config1: " + config1.getAppVersion());
        config2.setAppVersion("1.0.1-BETA"); // Change via one instance
        System.out.println("App Version from config1 after change via config2: " + config1.getAppVersion()); // Should reflect change
        config1.loadConfig();
        System.out.println("--- End Singleton Demonstration ---\n");
    }
}