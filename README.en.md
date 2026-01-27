# Harmony AI Code Service

## Project Overview

Harmony AI Code Service is an enterprise-level backend service project built on the Spring Boot framework, employing a standard layered architecture to implement core functionalities such as user management. The project integrates the MyBatis persistence framework, provides RESTful API interfaces, supports CORS configuration, and features a comprehensive exception handling mechanism.

## Technology Stack

- **Core Framework**: Spring Boot 2.x
- **Persistence Layer**: MyBatis + MyBatis-Plus
- **Database**: MySQL 5.7+ / 8.0+
- **Build Tool**: Maven
- **API Documentation**: Swagger (optional integration)
- **Logging**: Lombok @Slf4j

## Project Structure

```
harmony-ai-code/
├── src/main/java/com/harmony/harmonyaicodeservice/
│   ├── HarmonyAiCodeServiceApplication.java    # Main application class
│   ├── common/                                  # Common components
│   │   ├── BaseResponse.java                   # Unified response wrapper
│   │   ├── PageRequest.java                    # Pagination request parameters
│   │   ├── DeleteRequest.java                  # Delete request parameters
│   │   └── ResultUtils.java                    # Response utility class
│   ├── config/                                 # Configuration classes
│   │   └── CorsConfig.java                     # CORS configuration
│   ├── controller/                             # Controller layer
│   │   ├── HeatherController.java              # Health check endpoint
│   │   └── UserController.java                 # User management endpoints
│   ├── exception/                              # Exception handling
│   │   ├── BusinessException.java              # Business exception
│   │   ├── ErrorCode.java                      # Error code enumeration
│   │   ├── GlobalExceptionHandler.java         # Global exception handler
│   │   └── ThrowUtils.java                     # Exception throwing utility
│   ├── generator/                              # Code generator
│   │   └── MyBatisCodeGenerator.java           # MyBatis code generator
│   ├── mapper/                                 # Data access layer
│   │   └── UserMapper.java                     # User Mapper interface
│   ├── model/                                  # Data models
│   │   └── entity/
│   │       └── User.java                       # User entity class
│   └── service/                                # Business logic layer
│       ├── UserService.java                    # User service interface
│       └── impl/
│           └── UserServiceImpl.java            # User service implementation
├── src/main/resources/
│   ├── application.yml                         # Application configuration
│   └── mapper/                                 # MyBatis XML mappings
│       └── UserMapper.xml
├── sql/
│   └── create_table.sql                        # Database initialization script
├── pom.xml                                     # Maven configuration
└── README.md                                   # Project documentation
```

## Core Features

### User Management Module
- **User Registration**: Create new user accounts
- **User Query**: Retrieve detailed user information
- **User Update**: Modify user profile
- **User Deletion**: Delete specified users
- **User List**: Retrieve all user records
- **Paged Query**: Support pagination and sorting for user lists

### System Features
- **Health Check**: Heather endpoint for service health monitoring
- **Unified Response**: Standardized API response format
- **Exception Handling**: Centralized exception capture and handling
- **CORS Support**: Configurable Cross-Origin Resource Sharing

## Quick Start

### Prerequisites
- JDK 1.8 or higher
- Maven 3.6+
- MySQL 5.7+

### Database Configuration

1. Create the database:
```sql
CREATE DATABASE harmony_ai_code DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Execute the initialization script:
```bash
mysql -u username -p harmony_ai_code < sql/create_table.sql
```

### Project Build

```bash
# Clone the project
git clone https://gitee.com/Harmony_TL/harmony-ai-code.git

# Navigate to project directory
cd harmony-ai-code

# Compile the project
mvn clean compile

# Run the project
mvn spring-boot:run
```

### Configuration Details

Configure the database connection in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/harmony_ai_code?useUnicode=true&characterEncoding=utf8mb4
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## API Documentation

### Basic Information
- **Base URL**: `http://localhost:8080`
- **Content-Type**: `application/json`

### Endpoint List

#### 1. Health Check
```http
GET /heather
```
**Response Example**:
```json
{
  "code": 0,
  "data": "success",
  "message": "ok"
}
```

#### 2. User Management

| Method | Path | Description |
|--------|------|-------------|
| POST | `/user/save` | Create or save a user |
| DELETE | `/user/remove/{id}` | Delete a user |
| PUT | `/user/update` | Update user information |
| GET | `/user/list` | Retrieve user list |
| GET | `/user/getInfo/{id}` | Retrieve user details |
| GET | `/user/page` | Paginated user query |

### Unified Response Format

**Success Response**:
```json
{
  "code": 0,
  "data": { ... },
  "message": "ok"
}
```

**Error Response**:
```json
{
  "code": 40001,
  "data": null,
  "message": "Invalid request parameters"
}
```

## Core Class Descriptions

### BaseResponse<T>
Unified response wrapper containing:
- `code`: Status code (0 indicates success)
- `data`: Response data
- `message`: Informational message

### ErrorCode
Error code enumeration defining system-level error codes:
- `SUCCESS(0, "ok")`
- `PARAMS_ERROR(40000, "Invalid request parameters")`
- `NOT_LOGIN_ERROR(40100, "Not logged in")`
- `NO_AUTH_ERROR(40300, "Insufficient permissions")`
- `NOT_FOUND_ERROR(40400, "Requested data not found")`
- `SYSTEM_ERROR(50000, "System error")`

### ThrowUtils
Exception throwing utility class:
- `throwIf(condition, errorCode)`: Throws a business exception if condition is true
- `throwIf(condition, errorCode, message)`: Throws a business exception with a custom message if condition is true

## Code Generator

The project includes a built-in MyBatis code generator via `MyBatisCodeGenerator.java`, which can rapidly generate:
- Entity classes
- Mapper interfaces
- Service interfaces and implementations
- Controller classes

## License

This project is licensed under the MIT License.