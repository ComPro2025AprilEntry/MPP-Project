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

### 5.3 Technologies Used

-   **Backend:**
    -   Java 21
    -   Spring Boot (Web, Data JDBC, Security - if implemented for login)
    -   Maven (Build Automation)
    -   H2 Database (In-memory for development/testing, can be easily switched)
    -   JUnit 5 (Unit Testing)
    -   Bcrypt (for password hashing via Spring Security's `PasswordEncoder`)
-   **Frontend:**
    -   React.js
    -   Axios (HTTP Client)
    -   `react-toastify` (for notifications)
    -   `uuid` (for unique ID generation)
    -   JavaScript (ES6+)
    -   HTML/CSS

### 5.4 Layer Descriptions

-   **Presentation Layer (Frontend - React):**
    -   Composed of React components (e.g., `App.js`, `Login.js`, `Register.js`, `JobForm.js`, `JobList.js`, `JobStats.js`).
    -   Handles user input, displays data fetched from the backend, and manages local UI state.
    -   Uses Axios to make HTTP requests to the Spring Boot backend API.
    -   Provides user feedback through toast notifications.
-   **Service Layer (Backend - Spring `@Service`):**
    -   Includes `UserService.java` and potentially `JobService.java`.
    -   Encapsulates the business rules and logic (e.g., password encoding, checking for duplicate emails during registration, orchestrating job application operations).
    -   Acts as an intermediary between the controllers and the DAOs.
    -   Performs validation and throws custom exceptions (`EmailAlreadyExistsException`).
-   **Data Access Layer (Backend - Spring `@Repository`):**
    -   Includes `UserDAO.java` (interface) and `UserDAOImpl.java` (implementation), and `JobDAO.java` (interface) and `JobDAOImpl.java` (implementation).
    -   Provides an abstract API for database operations (CRUD, filtering, sorting, statistics).
    -   Uses `JdbcTemplate` to interact with the H2 database, abstracting raw SQL queries.
    -   Handles data mapping between database rows and Java objects.
-   **Database (H2):**
    -   An in-memory relational database used for development and testing.
    -   Stores `users` (id, username/email, password, name) and `applications` (id, company, position, techStack, appliedDate, deadline, status, userId) tables.
    -   Automatically initialized by the `JobDAOImpl` and `UserDAOImpl` at application startup.

---

## 6. Use Case Diagram(s)

_(Insert your use case diagram(s) here. It should visually represent the user (Actor) and the main functions they can perform within the system (Use Cases).)_

**Key Use Cases:**

-   **User Management:**
    -   Register Account
    -   Login to Account
-   **Job Application Management:**
    -   Add Job Application
    -   View All Job Applications
    -   Filter Job Applications by Tech Stack
    -   Sort Job Applications by Deadline
    -   Delete Job Application
    -   View Job Statistics

---

## 7. Use Case Descriptions

Here are detailed descriptions for some key use cases:

-   **Use Case Name:** Register Account

    -   **Primary Actor(s):** Unregistered User
    -   **Preconditions:** User is on the registration page; provided email is not already registered.
    -   **Postconditions:** A new user account is created; user is logged in and redirected to the dashboard.
    -   **Main Success Scenario:**
        1.  User navigates to the registration page.
        2.  User enters a unique email, password, and name.
        3.  User clicks "Register."
        4.  System validates input (e.g., non-empty fields).
        5.  System checks if the email already exists in the database.
        6.  If email is unique, system hashes the password and saves the new user details to the database.
        7.  System logs the user in (setting local storage token/user info).
        8.  System displays a success toast notification.
        9.  System redirects the user to the job applications dashboard.

-   **Use Case Name:** Add Job Application

    -   **Primary Actor(s):** Logged-in User
    -   **Preconditions:** User is logged in and on the job form/dashboard page.
    -   **Postconditions:** A new job application record is added to the database, associated with the user; the job list is updated.
    -   **Main Success Scenario:**
        1.  User fills out the job application form with details: company, position, tech stack, applied date, deadline (optional), status.
        2.  User clicks "Add Job."
        3.  System validates input (e.g., required fields are filled).
        4.  System formats the tech stack string into a list and generates a unique ID.
        5.  System sends the job data to the backend API.
        6.  Backend saves the job application to the database.
        7.  System displays a success toast notification ("Job application added successfully!").
        8.  System clears the form.
        9.  The job list automatically refreshes to show the new entry.

-   **Use Case Name:** Filter Job Applications by Tech Stack
    -   **Primary Actor(s):** Logged-in User
    -   **Preconditions:** User is logged in and viewing their job applications.
    -   **Postconditions:** The displayed list of job applications is filtered to show only those matching the specified tech stack (case-insensitive).
    -   **Main Success Scenario:**
        1.  User types a tech stack term (e.g., "React", "java") into the "Filter by Tech Stack" input field.
        2.  System debounces the input, waiting for a pause in typing (e.g., 500ms).
        3.  After the debounce period, system sends a request to the backend API with the user's ID and the entered tech stack.
        4.  Backend queries the database for applications whose tech stack matches the criteria (case-insensitively).
        5.  Backend returns the filtered list of job applications.
        6.  System updates the displayed job list with the filtered results.
        7.  The filter input remains visible and active throughout the process.

---

## 8. Class Diagram

_(Insert your UML class diagram image here. It should include the main classes like `User`, `JobApplication`, `UserDAO`, `JobDAO`, `UserService`, `JobService` (if implemented), `UserController`, `JobController`, showing their attributes, methods, and relationships (e.g., `User` has a one-to-many relationship with `JobApplication`, services depend on DAOs, controllers depend on services).)_

**Key Classes:**

-   **`User` (Model):** Represents a user with `id`, `username` (email), `password` (hashed), and `name`.
-   **`JobApplication` (Model):** Represents a single job application with `id`, `company`, `position`, `techStack` (List<String>), `appliedDate` (LocalDate), `deadline` (LocalDate), `status`, and `userId`.
-   **`UserDAO` (Interface) / `UserDAOImpl` (Repository):** Manages persistence for `User` objects. Methods: `findByUsername`, `findByEmail`, `save`.
-   **`JobDAO` (Interface) / `JobDAOImpl` (Repository):** Manages persistence for `JobApplication` objects. Methods: `save`, `deleteById`, `loadAll`, `filterByTechStack`, `sortByDeadline`, `getJobStatsByStatus`.
-   **`UserService` (Service):** Handles user-related business logic like registration (including email existence check, password encoding) and user retrieval.
-   **`JobController` (Controller):** REST endpoints for `JobApplication` management (GET, POST, DELETE, filter, sort, stats).
-   **`UserController` (Controller):** REST endpoints for `User` management (Register, Login).

---

## 9. Sequence Diagrams

_(Provide sequence diagrams for important use cases. You can create these using tools like PlantUML, Mermaid.js, or visual diagramming tools.)_

**Example Sequence Diagram Description: User Registration**

1.  **Actor (Unregistered User)**: Submits registration form (email, password, name) to **Frontend (Register Component)**.
2.  **Frontend (Register Component)**: Sends `POST /api/users/register` request (containing email, password, name) to **Backend (UserController)**.
3.  **Backend (UserController)**: Calls `userService.registerUser(user)` on **UserService**.
4.  **UserService**: Calls `userDAO.findByEmail(user.getUsername())` on **UserDAO**.
5.  **UserDAO**: Queries **Database (users table)** for existing email.
    -   **Alternative Flow (Email Exists):** Database returns user. UserDAO returns `Optional.of(user)`. UserService throws `EmailAlreadyExistsException`.
    -   **Alternative Flow (Email Exists - Backend):** UserService returns `EmailAlreadyExistsException` to UserController. UserController returns `409 Conflict` with error message to Frontend. Frontend displays specific error toast.
6.  **UserService**: If email is unique, encrypts password using `PasswordEncoder`.
7.  **UserService**: Calls `userDAO.save(user)` on **UserDAO**.
8.  **UserDAO**: Inserts new user into **Database**.
9.  **UserDAO**: Returns saved user to **UserService**.
10. **UserService**: Returns registered user to **UserController**.
11. **Backend (UserController)**: Returns `201 Created` response (excluding password) to **Frontend**.
12. **Frontend (Register Component)**: Stores user info locally, calls `onRegister`, and displays success toast notification.

**Example Sequence Diagram Description: Filtering Jobs by Tech Stack**

1.  **Actor (Logged-in User)**: Types into filter input on **Frontend (JobList Component)**.
2.  **Frontend (JobList Component)**: Updates `searchTerm` state.
3.  **Frontend (JobList Component - Debounce useEffect)**: After debounce delay, updates `debouncedFilterTechStack` state.
4.  **Frontend (JobList Component - Main useEffect)**: Detects change in `debouncedFilterTechStack`, triggers `loadJobs()`.
5.  **Frontend (JobList Component - loadJobs)**: Sets `isFetching` to `true`.
6.  **Frontend (JobList Component - loadJobs)**: Sends `GET /api/jobs/filter?userId={userId}&techStack={techStack}` request to **Backend (JobController)**.
7.  **Backend (JobController)**: Calls `jobService.filterJobsByTechStack(userId, techStack)` on **JobService**.
8.  **JobService**: Calls `jobDAO.filterByTechStack(userId, techStack)` on **JobDAO**.
9.  **JobDAO**: Executes SQL query (`SELECT ... WHERE techStack ILIKE ?`) on **Database (applications table)**.
10. **Database**: Returns filtered job applications.
11. **JobDAO**: Returns `List<JobApplication>` to **JobService**.
12. **JobService**: Returns filtered jobs to **JobController**.
13. **Backend (JobController)**: Returns `200 OK` response (List of jobs) to **Frontend**.
14. **Frontend (JobList Component - loadJobs)**: Sets `isFetching` to `false`, updates `jobs` state with new data.
15. **Frontend (JobList Component)**: Renders the updated list of jobs. A loading spinner/overlay disappears.

---

## 10. Screenshots

_(Include relevant screenshots of your application's interface and features here: e.g., Login Page, Register Page, Dashboard with Job Form, Job List, Filter in action, Sorted List, Job Statistics component.)_

---

## 11. Installation & Deployment

This project consists of two main parts: a Spring Boot backend and a React frontend.

### Prerequisites

-   Java Development Kit (JDK) 21 or higher
-   Maven (for backend)
-   Node.js (LTS version recommended, for frontend)
-   npm or Yarn (package manager for frontend)
-   Git

### Cloning the Repository

1.  Open your terminal or command prompt.
2.  Navigate to the directory where you want to clone the project.
3.  Run:
    ```bash
    git clone [https://github.com/ComPro2025AprilEntry/MPP-Project.git](https://github.com/ComPro2025AprilEntry/MPP-Project.git)
    cd MPP-Project
    ```

### Backend Setup and Running

1.  Navigate into the `backend/` directory:
    ```bash
    cd backend
    ```
2.  Build the project using Maven:
    ```bash
    mvn clean install
    ```
3.  Run the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```
    The backend will start on `http://localhost:8080` (by default). The H2 database will be an in-memory database, so no separate setup is needed, and data will reset on application restart unless configured otherwise.

### Frontend Setup and Running

1.  Open a **new terminal window** (keep the backend running in the first one).
2.  Navigate into the `frontend/` directory:
    ```bash
    cd frontend
    ```
3.  Install npm dependencies:
    ```bash
    npm install
    ```
4.  Create a `.env` file in the `frontend/` directory if you need to specify the backend URL (e.g., for deployment, but optional for local development if backend is on 8080):
    ```
    REACT_APP_API_BASE_URL=http://localhost:8080/api
    ```
5.  Start the React development server:
    ```bash
    npm start
    ```
    The frontend will open in your browser, typically at `http://localhost:3000`.

### (Optional) Docker Setup Instructions

_(If you have provided Dockerfiles for your backend and/or frontend, include instructions here for building and running them using Docker Compose or individual `docker build` and `docker run` commands.)_

---

## 12. How to Use

Once both the backend and frontend applications are running:

1.  **Access the Application:** Open your web browser and go to `http://localhost:3000`.
2.  **Register:**
    -   Click on the "Register" link if you don't have an account.
    -   Enter a unique email, a password, and your name.
    -   Click "Register." You should see a success toast notification. If the email is already in use, a specific error message will appear.
3.  **Login:**
    -   If you just registered, you will be automatically logged in. Otherwise, navigate to the login page.
    -   Enter your registered email and password.
    -   Click "Login."
4.  **Add a Job Application:**
    -   After logging in, you'll see the "Add New Application" form.
    -   Fill in details like Company, Position, Tech Stack (e.g., "React, Redux, Node.js"), Applied Date, and select a Status. Deadline is optional.
    -   Click "Add Job." A success toast will confirm the addition, and the form will clear.
5.  **View and Manage Applications:**
    -   Your added applications will appear in the "Your Applications" list below the form.
    -   Click the "Delete" button next to any job to remove it.
6.  **Filter by Tech Stack:**
    -   Use the "Filter by Tech Stack" input field. Type in a technology (e.g., "Java", "SQL", "React").
    -   The list will dynamically filter as you type (after a short pause), showing only applications containing that tech stack (case-insensitive).
7.  **Sort by Deadline:**
    -   Click the "Sort by Deadline" button. Your applications will be sorted by their deadline in ascending order (applications without a deadline will be listed last).
    -   Click the button again to "Unsort" and revert to the default order.
    -   Note: Sorting clears any active filter, and filtering clears any active sort.
8.  **View Job Statistics:**
    -   The "Job Application Statistics" section displays a count of your applications grouped by their status (e.g., Applied, Interviewing, Offer). This updates automatically when you add or delete jobs.

---

## 13. Design Justification & Principles

-   **Layered Architecture:** Adherence to this pattern ensures clear separation of concerns (Presentation, Service, Data Access, Database). This improves modularity, making the codebase easier to understand, test, and maintain. For example, changes in the UI logic don't affect the backend business logic.
-   **RESTful API Design:** The backend exposes a RESTful API, enabling a clear contract between frontend and backend. This promotes stateless communication and scalability.
-   **Single Responsibility Principle (SRP - from SOLID):** Each class and component has a single, well-defined responsibility.
    -   `JobForm` handles form input.
    -   `JobList` manages displaying and interacting with the list.
    -   `JobController` handles HTTP requests for jobs.
    -   `JobService` contains job-related business logic.
    -   `JobDAOImpl` handles database operations for jobs.
-   **Open/Closed Principle (OCP - from SOLID):** The design allows for extensions without modification of existing, working code. For instance, adding a new filter option on the backend might involve adding a new method to `JobDAO` and `JobService`, but existing methods would remain unchanged. Frontend components are designed to accept new features via props and conditional rendering.
-   **Dependency Inversion Principle (DIP - from SOLID):** High-level modules (e.g., `UserService`) do not depend on low-level modules (e.g., `UserDAOImpl`) directly but on abstractions (the `UserDAO` interface). This allows for easier testing (mocking DAOs) and flexibility to switch database implementations without altering service logic.
-   **Use of Interfaces (`UserDAO`, `JobDAO`):** Provides abstraction for data access operations, allowing for different implementations (e.g., different databases) without changing the service layer.
-   **Java Stream API:** Leveraged for declarative and functional-style data processing, leading to more readable and concise code for collection manipulations (filtering, mapping, aggregation) in the service and DAO layers.
-   **`useCallback` and `useEffect` in React:** Used to optimize component rendering performance and manage side effects effectively, preventing unnecessary re-renders and ensuring correct hook dependencies.
-   **Debouncing:** Implemented on the frontend filter input to prevent excessive API calls and improve user experience during typing.
-   **Controlled Components (React):** All form elements are controlled by React state, ensuring predictable behavior and easier validation.
-   **User Feedback (Toast Notifications):** Replaces disruptive `alert()` calls with `react-toastify` for a non-blocking, customizable, and visually appealing way to inform users about success or error states.
-   **Explicit Singleton Pattern (`ConfigurationManager`):** As per requirements, the Singleton pattern is explicitly demonstrated in the `ConfigurationManager` utility class. This class ensures only one instance of itself is created throughout the application's lifecycle, providing a single global point of access for application-wide configurations (e.g., application version, API base URL). This is achieved through a private constructor and a public static `getInstance()` method, ensuring thread-safe instantiation. While Spring's default bean scope is singleton and typically preferred for managing most application components due to its integration with dependency injection, this explicit implementation serves as a clear example of the pattern's mechanics.

---

## 15. References

-   Spring Boot Documentation: [https://spring.io/guides/](https://spring.io/guides/)
-   React Documentation: [https://react.dev/](https://react.dev/)
-   Axios GitHub: [https://github.com/axios/axios](https://github.com/axios/axios)
-   React Toastify: [https://fkhadra.github.io/react-toastify/](https://fkhadra.github.io/react-toastify/)
-   uuid npm: [https://www.npmjs.com/package/uuid](https://www.npmjs.com/package/uuid)
-   Software Architecture Patterns (O'Reilly): [https://www.oreilly.com/library/view/software-architecture-patterns/9781491971437/ch01.html](https://www.oreilly.com/library/view/software-architecture-patterns/9781491971437/ch01.html)
