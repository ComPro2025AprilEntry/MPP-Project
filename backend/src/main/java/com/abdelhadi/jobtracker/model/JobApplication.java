package com.abdelhadi.jobtracker.model;

import java.time.LocalDate;
import java.util.List;

public class JobApplication {
    private String id;
    private String company;
    private String position;
    private List<String> techStack; // Handled as comma-separated string in DAO
    private LocalDate appliedDate;
    private LocalDate deadline;
    private String status;
    private String userId;

    public JobApplication() {}

    public JobApplication(String id, String company, String position, List<String> techStack,
                          LocalDate appliedDate, LocalDate deadline, String status, String userId) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.techStack = techStack;
        this.appliedDate = appliedDate;
        this.deadline = deadline;
        this.status = status;
        this.userId = userId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public List<String> getTechStack() { return techStack; }
    public void setTechStack(List<String> techStack) { this.techStack = techStack; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}