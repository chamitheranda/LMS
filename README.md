# Learning Management System

## Overview

This project is a Learning Management System (LMS) developed using Spring Boot with a layered architecture, MySQL with MyBatis for data storage, JWT for authentication, BCrypt encoder for password encryption, and a logging mechanism for system monitoring. The system includes four controllers, each with specific endpoints, and implements unit tests for all controllers.

## Tech Stack

- Spring Boot with Layered Architecture
- MySQL with MyBatis
- JWT for Authentication
- BCrypt Encoder for Password Encryption

## Controllers and Endpoints

### Admin Controller

Endpoints:
- `updatePrivilegeLevel`
- `updateResults`
- `deleteUser`

### Auth Controller

Endpoints:
- `signIn`
- `signUp`

### Result Controller

Endpoints:
- `addResults`
- `viewResults`

### User Controller

Endpoints:
- `enrollCourses`

## Authentication with JWT

JWT (JSON Web Token) is used for authentication. After a successful login:
- JWT is generated with a `userPrivilege` claim.
- `userPrivilege` can be:
  - "admin" for admin users
  - "none" for non-admin users
- User email is extracted from the subject of the JWT for data integrity.

## Security Measures

- Passwords are securely stored using BCrypt encoder.
- JWT is used for user authentication and authorization.
- User privilege level is utilized for access control.
- Logging mechanism is implemented for system monitoring.

## Unit Testing

All controllers have undergone unit testing using Mockito and JUnit. The tests ensure correct behavior and robust functionality.

## Usage

1. Clone the repository.
2. Configure the database details in the application properties.
3. Build and run the application.

## Contributing

Feel free to contribute to the project. Create a pull request with your changes.


