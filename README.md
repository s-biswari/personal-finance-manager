# Personal Finance Manager

## Overview
The Personal Finance Manager is a Spring Boot application designed to help users manage their personal finances. It provides features for user authentication, secure access to financial data, and the ability to track expenses and income.

## Features
- User registration and login
- JWT-based authentication
- Secure access to financial data
- User-friendly interface for managing finances

## Technologies Used
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA
- H2 Database (or any other database of your choice)

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven

### Installation
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```
   cd personal-finance-manager
   ```
3. Build the project using Maven:
   ```
   mvn clean install
   ```

### Configuration
1. Open `src/main/resources/application.properties` and configure your database settings and JWT secret key.

### Running the Application
1. Run the application:
   ```
   mvn spring-boot:run
   ```
2. Access the application at `http://localhost:8080`.

## Usage
- Register a new user by sending a POST request to `/api/auth/register`.
- Log in by sending a POST request to `/api/auth/login` to receive a JWT token.
- Use the token to access protected endpoints by including it in the Authorization header as `Bearer <token>`.

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for details.