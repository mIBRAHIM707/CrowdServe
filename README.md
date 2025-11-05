# CrowdServe

A localized, real-time crowdsourcing platform built with Spring Boot 3 and Java 17.

## Overview

CrowdServe is a modern web application that enables users to post tasks and connect with workers who can complete them. The platform provides a secure, efficient way to manage crowdsourced tasks with real-time updates and comprehensive reporting.

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** - Database access and ORM
- **Spring Security** - Authentication and authorization
- **Thymeleaf** - Server-side templating
- **H2 Database** - In-memory database for development
- **PostgreSQL** - Production database
- **Lombok** - Boilerplate reduction
- **Maven** - Build and dependency management

## Project Structure

```
com.crowdserve
├── config          # Security and application configuration
├── dto             # Data Transfer Objects
├── model           # JPA entities
├── repository      # Data access layer
└── service         # Business logic layer
```

## Features

- User registration and authentication
- Task creation and management
- Task assignment workflow
- Task status tracking (OPEN, ASSIGNED, COMPLETED, CANCELLED)
- Reporting and analytics

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### H2 Console

In development mode, you can access the H2 database console at:
`http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:crowdserve`
- Username: `sa`
- Password: (leave blank)

## Configuration

### Development Profile (default)

Uses H2 in-memory database. Configuration in `application.properties`.

### Production Profile

Uses PostgreSQL database. Configuration in `application-prod.properties`.

To run with production profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Security

- Passwords are encrypted using BCrypt
- Form-based authentication
- Public endpoints: `/register`, `/login`, `/css/**`, `/js/**`
- All other endpoints require authentication

## Database Schema

### Users Table
- User accounts with email-based login
- Encrypted passwords
- Full name for display

### Tasks Table
- Task title and description
- Status tracking
- Poster (creator) reference
- Worker (assignee) reference

## Next Steps

This is the foundational skeleton. Future enhancements will include:

- Service layer implementations
- Web controllers
- Thymeleaf templates
- Task assignment and completion logic
- Reporting functionality
- Real-time updates
- Geolocation features

## License

[Your License Here]

## Contributors

[Your Name/Team]
