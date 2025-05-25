# Personal Finance Manager

## Overview
The Personal Finance Manager is a Spring Boot application designed to help users manage their personal finances. It provides features for user authentication, secure access to financial data, and the ability to track expenses and income.

## Features
- User registration and login
- JWT-based authentication
- Secure access to financial data
- User-friendly interface for managing finances
- Budget, Category, and Transaction management
- Spending and budget reports

## Technologies Used
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA
- PostgreSQL (or H2 for testing)
- Maven
- JaCoCo (for test coverage)

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven

### Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd personal-finance-manager
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

### Configuration
1. Open `src/main/resources/application.properties` and configure your database settings and JWT secret key.

### Running the Application
1. Run the application:
   ```bash
   mvn spring-boot:run
   ```
2. Access the application at `http://localhost:8080`.
3. API documentation is available at `/swagger-ui.html`.

## API Endpoints

### Auth
- **Register:**
  - `POST /api/auth/register`
  - Body:
    ```json
    {
      "username": "newuser@example.com",
      "password": "password",
      "email": "newuser@example.com",
      "role": "ROLE_USER"
    }
    ```
- **Login:**
  - `POST /api/auth/login`
  - Body:
    ```json
    {
      "username": "user@example.com",
      "password": "password"
    }
    ```

### Budgets
- **Get All:** `GET /api/budgets`
- **Set Budget:** `POST /api/budgets?categoryId=1&month=5&year=2025&amount=1000`
- **Delete:** `DELETE /api/budgets/{id}`
- **Monthly Report:** `GET /api/budgets/report?month=5&year=2025`

### Categories
- **Get All:** `GET /api/categories`
- **Get By Id:** `GET /api/categories/{id}`
- **Create:**
  - `POST /api/categories`
  - Body:
    ```json
    {
      "name": "TestCat"
    }
    ```
- **Update:**
  - `PUT /api/categories/{id}`
  - Body:
    ```json
    {
      "id": 1,
      "name": "UpdatedCat"
    }
    ```
- **Delete:** `DELETE /api/categories/{id}`

### Transactions
- **Get All:** `GET /api/transactions`
- **Get By Id:** `GET /api/transactions/{id}`
- **Create:**
  - `POST /api/transactions`
  - Body:
    ```json
    {
      "description": "Test Transaction",
      "amount": 100,
      "date": "2025-05-25",
      "categoryId": 1
    }
    ```
- **Update:**
  - `PUT /api/transactions/{id}`
  - Body:
    ```json
    {
      "description": "Updated Transaction",
      "amount": 200,
      "date": "2025-05-25",
      "categoryId": 1
    }
    ```
- **Delete:** `DELETE /api/transactions/{id}`
- **Spending By Category:** `GET /api/transactions/spending?month=5&year=2025`

### Reports
- **Download Spending Report (PDF/Excel):**
  - `GET /download-report?month=5&year=2025&format=pdf`
  - `GET /download-report?month=5&year=2025&format=excel`

### Health
- **Health Check:** `GET /api/health`

## Usage
- Register a new user by sending a POST request to `/api/auth/register`.
- Log in by sending a POST request to `/api/auth/login` to receive a JWT token.
- Use the token to access protected endpoints by including it in the Authorization header as `Bearer <token>`.
- Use the provided Postman collection (`PersonalFinanceManager_AllEndpoints.postman_collection.json`) to test all endpoints.

## Test Coverage
To check test coverage using Maven and JaCoCo:

1. Add the JaCoCo plugin to your `pom.xml` (if not already present):
   ```xml
   <plugin>
     <groupId>org.jacoco</groupId>
     <artifactId>jacoco-maven-plugin</artifactId>
     <version>0.8.8</version>
     <executions>
       <execution>
         <goals>
           <goal>prepare-agent</goal>
         </goals>
       </execution>
       <execution>
         <id>report</id>
         <phase>test</phase>
         <goals>
           <goal>report</goal>
         </goals>
       </execution>
     </executions>
   </plugin>
   ```
2. Run tests with coverage:
   ```bash
   mvn clean test
   ```
3. View the coverage report:
   - Open `target/site/jacoco/index.html` in your browser for a detailed report.

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for details.