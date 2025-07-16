package com.abdelhadi.jobtracker.util;

// This class demonstrates the classic explicit Singleton pattern.
// In a real Spring Boot application, most services and DAOs are Spring-managed
// singletons by default (via @Service, @Repository, @Component) and you
// would typically rely on Spring's IoC container for this.
// This explicit example is for demonstration purposes as requested.
public final class ConfigurationManager { // 'final' prevents subclassing

    private static ConfigurationManager instance; // The single instance
    private String appVersion;
    private String apiBaseUrl;

    // Private constructor to prevent direct instantiation
    private ConfigurationManager() {
        // Initialize default configuration values
        this.appVersion = "1.0.0";
        this.apiBaseUrl = "http://localhost:8080/api";
        System.out.println("ConfigurationManager: Initializing (first time only)");
    }

    // Public static method to get the single instance
    public static synchronized ConfigurationManager getInstance() {
        // 'synchronized' ensures thread safety during first initialization
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    // Example getter methods
    public String getAppVersion() {
        return appVersion;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    // Example setter methods (if configuration can be changed at runtime)
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    // You can also add a method to load config from a file, etc.
    public void loadConfig() {
        // Example: load from a properties file or environment variables
        System.out.println("ConfigurationManager: Loading application configuration...");
        // For demonstration, let's just log current values
        System.out.println("  App Version: " + appVersion);
        System.out.println("  API Base URL: " + apiBaseUrl);
    }
}
