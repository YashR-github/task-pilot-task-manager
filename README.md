# TaskPilot TaskManager

A robust, feature-rich backend application designed for personalized task management, integrating advanced filtering, dynamic notifications (Kafka/SMTP), time tracking, and experimental AI-powered natural language querying.

## 2. Badges

| Status | Details |
| :--- | :--- |
| **Language** | ![Java 17](https://img.shields.io/badge/Java-17-blue) |
| **Framework** | ![Spring Boot 3.x](https://img.shields.io/badge/Spring%20Boot-3.4.5-green) |
| **Database** | ![MySQL](https://img.shields.io/badge/Database-MySQL-blue) |
| **Messaging** | ![Kafka (Optional)](https://img.shields.io/badge/Messaging-Kafka-lightgrey) |
| **Security** | ![JWT Secured](https://img.shields.io/badge/Security-JWT%20Auth-orange) |
| **Documentation** | ![OpenAPI](https://img.shields.io/badge/Docs-Springdoc%20OpenAPI-yellowgreen) |


## 3. Project Overview

TaskPilot is a high-performance backend system built on Spring Boot 3.x, focused on providing secure, scalable, and auditable task management capabilities. The core problem solved is managing complex user workflows—tasks with sub-checklists, detailed time estimation, and mandatory time tracking—all while ensuring data isolation and providing versatile communication channels.

The project features decoupled asynchronous/synchronous notification mechanisms and implements advanced querying using the JPA Specification pattern. A notable feature is the secure integration with Google Gemini, allowing users to query their private task data using natural language, safeguarded by row-level security enforcement.

## 4. Key Features

The following features are derived directly from the application's controllers and services:

*   **User Authentication & Security:** Implements JWT token generation, validation, and authorization using Spring Security, securing all task endpoints. Passwords are secured using `BCryptPasswordEncoder`.
*   **Comprehensive Task Management:** Supports CRUD operations for tasks, tracking task attributes such as `TaskPriority` and `TaskStatus` (e.g., NOT\_STARTED, INPROGRESS, COMPLETED).
*   **Dynamic Task Filtering (JPA Specification):** Offers a single unified endpoint for complex querying, supporting combination filters across multiple fields including task name, status, priority, category details, keyword searching, and creation date ranges.
*   **Time Tracking and Auditing:**
    *   Uses JPA Auditing (`Basemodel`) to automatically populate `createdAt` and `updatedAt` timestamps.
    *   Allows users to log task start/completion times (`startDateTime`, `endDateTime`).
    *   Calculates `totalTimeTaken` and provides a comparison remark against the `expectedTaskTime`.
*   **Checklist Management:** Supports creating, retrieving, deleting, starting, and completing associated `ChecklistItem` subtasks for any main task.
    *   **Constraint Enforcement:** Logic prevents users from marking a main task complete if associated checklist items are still pending.
    *   **Time Validation:** Enforces that the total estimated time of all checklist items does not exceed the estimated time of the parent task (`ChecklistTimeExceedsException`).
*   **Configurable Notification System:** Uses a **Strategy Pattern** via `UnifiedNotificationDispatcher`. The system can be toggled by the `use.kafka` property to operate in two modes:
    1.  **Asynchronous (Kafka):** Dispatches email requests to a Kafka topic for processing by an `EmailKafkaConsumer`.
    2.  **Synchronous (Direct):** Immediately sends emails using either the configured SendGrid API or SMTP server (switchable via `email.provider`).
*   **Scheduled Reminders:** A daily scheduler (`ReminderScheduler`) fetches all users and their pending tasks (`TaskStatus.COMPLETED` excluded) and dispatches daily reminder emails via the unified notification system.
*   **AI Data Integration (Secure NL-to-SQL):** The `ChatbotIntegrationController` allows users to submit natural language prompts which are converted to runnable SQL queries via the Gemini API. The integration ensures data security by forcing the AI to include `WHERE user_id = %d` in all generated queries, limiting data access to the authenticated user.

## 5. Tech Stack

| Category | Technology | Version | Source Evidence |
| :--- | :--- | :--- | :--- |
| **Backend Framework** | Spring Boot | 3.4.5 (Parent) | `pom.xml` |
| **Language** | Java | 17 | `pom.xml` |
| **Persistence** | Spring Data JPA, Hibernate | N/A | `pom.xml` |
| **Database** | MySQL | N/A | `pom.xml`, `application.properties` |
| **Messaging** | Spring Kafka | N/A | `pom.xml` |
| **Security** | Spring Security, JJWT | 0.11.5 | `pom.xml` |
| **External Services** | Google Gemini (AI) | 1.0.0 | `pom.xml`, `GeminiIntegrationService` |
| **Email Providers** | SendGrid, JavaMailSender (SMTP) | 4.10.3 (SendGrid) | `pom.xml`, `application.properties` |
| **Documentation** | Springdoc OpenAPI UI | 1.7.0 | `pom.xml` |
| **Utilities** | Lombok, Validation | N/A | `pom.xml`, `jakarta.validation` imports |


## 6. Architecture

TaskPilot employs a layered service architecture utilizing Spring framework components heavily for decoupling and advanced functionality.

### Core Architectural Decisions:

1.  **Auditing and Soft Deletes:** All entities (User, Task, Category, etc.) inherit from `Basemodel`, which utilizes `@EntityListeners(AuditingEntityListener.class)` to automate the tracking of `createdAt` and `updatedAt`, and includes a `isDeleted` flag for optional soft deletion.
2.  **Pluggable Notifications (Strategy Pattern):** The `UnifiedNotificationDispatcher` acts as a facade, deciding whether to use the Kafka producer (`NotificationDispatcher`) or direct email sender (`DirectNotificationDispatcher`) based on the runtime property `use.kafka`. This allows the application to switch seamlessly between asynchronous and synchronous communication based on deployment needs.
3.  **Secure AI Layer:** The `GeminiIntegrationService` uses a pre-cached schema (`SchemaCache`) to structure its prompts, guaranteeing that the AI generates valid, production-ready SQL. Security is hardcoded by injecting the authenticated user's ID (`userId`) into the LLM prompt to ensure all generated queries contain a mandatory `WHERE user_id = %d` clause.


## 7. Prerequisites

To run this application, you must have the following installed:

*   **Java Development Kit (JDK):** Version 17 or higher.
*   **Maven:** For dependency management and building (`pom.xml` present).
*   **MySQL Database:** The application is configured to connect to an external MySQL instance.
*   **Kafka (Optional):** If `use.kafka=true`, a local Kafka broker must be running on `localhost:9092`.


## 8. Installation & Setup

### 8.1. Clone Repository

```bash
git clone <repository_url>
cd TaskPilot_TaskManager
```

### 8.2. Environment Configuration

You must set the following critical environment variables or replace the placeholders in `application.properties` (or your preferred environment manager):

| Variable | Purpose | Source |
| :--- | :--- | :--- |
| `JWT_SECRET` | Secret key for JWT token signing. | `application.properties` |
| `GEMINI_API_KEY` | API Key for accessing Google Gemini services. | `application.properties` |
| `AWS_MYSQL_RDS_DB_PASS` | Password for MySQL Database access. | `application.properties` |
| `SPRINGMAIL_GMAIL_APP_USER` | Gmail username (if `email.provider=smtp`). | `application.properties` |
| `SPRINGMAIL_GMAIL_APP_PASS` | Gmail app password (if `email.provider=smtp`). | `application.properties` |
| `SENDGRID_API_KEY` | SendGrid API Key (if `email.provider=sendgrid`). | `application.properties` |
| `sendgrid.from.email` | Sender email address. | `application.properties` |

**Configuration Toggle (`application.properties`):**

The application default configuration uses direct email dispatch (SMTP). If you wish to use Kafka, update these properties:

```properties
# To enable Kafka queue dispatch, mark it as true:
use.kafka=true
# choose email provider irrespective of use.kafka value, choose out of sendgrid and smtp (note: both require additional setup of access keys such as sendgrid api key or gmail app password):
email.provider=smtp
# Spring Kafka bootstrap servers configured below (default: localhost:9092)
```


### 8.3. Building and Running

1.  **Build the Project:**
    ```bash
    mvn clean install
    ```
2.  **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```

The application will start on the default Spring Boot port (typically 8080).

## 9. Usage Guide

### Authentication

All endpoints, except those under `/auth`, require a JWT Bearer token.

| Endpoint | Method | Purpose |
| :--- | :--- | :--- |
| `/auth/signup` | `POST` | Registers a new user. |
| `/auth/login` | `POST` | Authenticates user (by username or email) and returns a JWT in the Authorization Header. |

### Interaction

Once authenticated, include the JWT in the `Authorization: Bearer <token>` header for all subsequent API calls.

## 10. API Documentation (Key Endpoints)

The following core functional routes are available:

### Task Management (`/tasks`)

| Endpoint | Method | Purpose | Details |
| :--- | :--- | :--- | :--- |
| `/tasks` | `POST` | Create a new task. | Requires Task name, category code, priority, and expected time estimate. |
| `/tasks/{taskId}` | `PATCH` | Update existing task details. | Allows updating name, description, priority, category code, and expected time. |
| `/tasks` | `GET` | **Advanced Filtering/Search**. | Uses the JPA Specification pattern for combined filtering, sorting, pagination, and keyword search. |
| `/tasks/{taskId}` | `DELETE`| Delete a task. | Triggers email notification upon successful deletion. |
| `/tasks/{taskId}/log-start` | `PATCH` | Log task starting time. | Sets `startDateTime` and changes `taskStatus` to INPROGRESS. |
| `/tasks/{taskId}/mark-task-complete` | `PATCH` | Mark task complete. | Sets `taskStatus` to COMPLETED, marks *all* associated checklist items as complete, and calculates total time taken. |

### Checklist Item Management (`/task/{taskId}/checklist-items`)

| Endpoint | Method | Purpose |
| :--- | :--- | :--- |
| `/task/{taskId}/checklist-items` | `POST` | Create one or more checklist items for the given task. |
| `/task/{taskId}/checklist-items/{checklistId}`| `GET` | Retrieve a single checklist item. |
| `/task/{taskId}/checklist-items/{checklistId}/start` | `PATCH` | Log start of a checklist item. |
| `/task/{taskId}/checklist-items/{checklistId}/mark-checklist-item-complete` | `PATCH` | Mark a checklist item as complete. |
| `/task/{taskId}/checklist-items/{checklistId}`| `DELETE` | Delete a checklist item. |

### Category Management (`/category`)

| Endpoint | Method | Purpose |
| :--- | :--- | :--- |
| `/category` | `POST` | Create a custom task category. |
| `/category` | `GET` | Retrieve all categories belonging to the user. |
| `/category/{id}` | `PATCH` | Update category details. |
| `/category/{id}` | `DELETE` | Delete a category. |

### AI Integration (`/api/chatbot`)

| Endpoint | Method | Purpose |
| :--- | :--- | :--- |
| `/api/chatbot/query` | `POST` | Submit a natural language prompt to query task data. |

## 11. Contributing

If you wish to contribute to TaskPilot TaskManager, please follow these steps:

1.  Fork the repository.
2.  Create your feature branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request.

## 12. License

Note: This project is licensed under the MIT License.
