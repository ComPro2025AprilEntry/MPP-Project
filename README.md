# Job Application Tracker

**Course:** Modern Programming Practices

**Block:** July 2025

**Instructor:** Dr. Bright Gee Varghese R

**Team Members:**

Oumar Mahamat Abdelhadi 619566

**Date of Submission:** 07/16/2025

---

# Important Project Requirements

**Stream API:**

The Java Stream API has been extensively utilized throughout the backend for efficient and declarative data processing. Key areas of usage include:

-   **`JobDAOImpl.java`**: Filtering job applications (e.g., `findByEmail`, `loadAll`, `filterByTechStack`, `sortByDeadline`), aggregation for statistics (`getJobStatsByStatus`), and processing results from JDBC queries.
-   **`UserService.java`**: Logic for checking user existence and managing collections of users (though less prominent here compared to job applications).
-   **Data Transformation**: Converting `List<String>` for `techStack` to comma-separated `String` for database storage and vice-versa.

These usages are clearly highlighted in the respective code files and contribute to a more concise and readable codebase.

**Unit Testing:**

Unit tests are implemented for critical business logic in the backend, primarily focusing on `UserService` and `JobService` (if created) or directly on `JobDAOImpl` methods. These tests ensure the core functionalities like user registration, job addition, deletion, filtering, sorting, and statistics calculation work as expected and cover various scenarios, including edge cases (e.g., duplicate email registration, deleting non-existent jobs).

-   **Instructions for Running Tests:**
    1.  Navigate to the `backend/` directory in your terminal.
    2.  Run the Maven test command: `mvn test`
    3.  JUnit reports will be generated, indicating test success or failure.

**Singleton Pattern:**

The Singleton design pattern is implicitly applied through Spring Framework's dependency injection container. Spring beans, by default, are Singletons within the application context. This means classes like `UserDAOImpl`, `JobDAOImpl`, `UserService`, and `JobService` (if exists) are managed as single instances by Spring, ensuring a single point of access for their respective functionalities (e.g., database connections, service logic). This approach aligns with best practices for managing resources efficiently in a Spring Boot application without manual Singleton implementation.

---

## 1. Problem Description

The problem addressed by this project is the lack of an organized and efficient way for individuals to track their job applications. Manually managing applications across various platforms, keeping track of deadlines, application statuses, and relevant technologies (tech stack) can be overwhelming and lead to missed opportunities. This application provides a centralized, user-specific platform to store, manage, filter, sort, and visualize their job application progress, reducing administrative burden and improving the job search experience.

---

## 2. User Stories

The system is designed to meet the following user needs:

-   **As a Job Seeker**, I want to **register for an account** so that I can securely store my job application data.
-   **As a Job Seeker**, I want to **log in to my account** so that I can access my personalized job application dashboard.
-   **As a Job Seeker**, I want to **add new job applications** (including company, position, tech stack, applied date, deadline, status) so that I can maintain a comprehensive record of my applications.
-   **As a Job Seeker**, I want to **view a list of all my job applications** so that I can quickly see my overall application status.
-   **As a Job Seeker**, I want to **delete existing job applications** so that I can remove outdated or irrelevant entries.
-   **As a Job Seeker**, I want to **filter my job applications by tech stack (case-insensitive)** so that I can easily find applications related to specific technologies.
-   **As a Job Seeker**, I want to **sort my job applications by deadline** so that I can prioritize applications that require immediate attention.
-   **As a Job Seeker**, I want to **view statistics on my job applications grouped by status** so that I can understand my progress at a glance (e.g., how many are "Applied", "Interviewing", "Offer").
-   **As a Job Seeker**, I want to receive **clear and non-disruptive feedback** (e.g., toast notifications) when I perform actions like adding or deleting a job, or when an error occurs, so that I have a smooth user experience.
-   **As a Job Seeker**, I want the **filter input to be responsive but not refresh on every keystroke**, and **remain visible** even if no results are found, so that my filtering experience is smooth and consistent.

---

## 3. Functional Requirements

-   User Registration and Authentication (Login).
-   Ability to add a new job application with details: company, position, tech stack (list), applied date, deadline, status.
-   Ability to retrieve all job applications for a specific user.
-   Ability to delete a job application by ID.
-   Ability to filter job applications by a specified tech stack (case-insensitive).
-   Ability to sort job applications by deadline (ascending, nulls last).
-   Ability to retrieve aggregated statistics of job applications grouped by their status.
-   Secure storage of user passwords (hashed).
-   Unique email (username) enforcement for user registration.
-   Input validation for job application fields.

---

## 4. Non-Functional Requirements

-   **Usability:** Intuitive user interface, clear navigation, real-time feedback (toast notifications), non-disruptive filtering (debouncing), persistent filter input.
-   **Security:** Password hashing, user-specific data access (users can only see their own jobs).
-   **Reliability:** Robust error handling in both frontend and backend for API calls and database operations.
-   **Performance:** Efficient database queries, debounced search to reduce API calls.
-   **Maintainability:** Modular and layered architecture, clean code, unit tests.
-   **Scalability:** Designed with separate frontend and backend, allowing independent scaling.

---

## 5. Architecture of Project

### 5.1 Overview

The project follows a standard **Layered Architecture** (also known as N-tier architecture), separating concerns into distinct logical layers. This promotes modularity, maintainability, and scalability.

-   **Presentation Layer (Frontend):** Handles the user interface and user interactions.
-   **Service Layer (Backend):** Contains the core business logic, orchestrates data flow, and interacts with the Data Access Layer.
-   **Data Access Layer (Backend):** Responsible for direct interaction with the database, abstracting data storage details from the service layer.
-   **Database:** Persistent storage for application data.

### 5.2 Architecture Diagram

_(Insert a diagram here illustrating a typical 3-tier architecture with Client (React), Backend (Spring Boot with Controller, Service, DAO), and Database (H2). You can use tools like draw.io, Lucidchart, or Diagrams.net to create this. A conceptual diagram similar to the O'Reilly one you linked, but specific to your components, would be ideal.)_

Example structure:
