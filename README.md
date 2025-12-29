# Restaurant Reservation System

This project is a **Restaurant Reservation System** composed of two Spring Boot services and a gateway, built with **Java 25** and **Spring Boot 3**.

restaurant-manager` and `reservation-manager are structured using **Hexagonal Architecture (Ports & Adapters)** to ensure strong domain isolation and testability.

Maven is used as a package manager.

## Services

### 1. Restaurant Manager
- **Technology**: Java 25, Spring Boot 3
- **Port**: `8081`
- **Swagger UI**:  
  http://localhost:8081/swagger-ui/index.html
- **Responsibilities**:
    - Manage restaurant data
    - Expose REST APIs consumed by other services

---

### 2. Reservation Manager
- **Technology**: Java 25, Spring Boot 3
- **Port**: `8083`
- **Swagger UI**:  
  http://localhost:8083/swagger-ui/index.html
- **Responsibilities**:
    - Manage reservations
    - Communicate with `restaurant-manager` via REST
- **HTTP Client**:
    - Uses `RestClient` to consume `restaurant-manager` APIs

---

### 3. API Gateway
- **Technology**: Java 25, Spring Boot 3
- **Port**: `8080`
- **Swagger UI**:  
  http://localhost:8080/swagger-ui/index.html
- **Responsibilities**:
    - Centralized routing to backend services
    - JWT-based security
- **Authentication Endpoint**:
    - Token generation:  
      `POST http://localhost:8080/auth`

---

## Persistence

- **Database**: PostgreSQL 17, it is one of the possible databases that fits our system requirements
- **Runtime Database**:
    - Launched via `docker-compose`
    - The `docker-compose.yml` file is located in the **restaurant-manager** project
- **Identifiers**:
    - UUID is used as the primary identifier format
- **Indexing**:
    - Indexes are defined on selected database fields for performance

---

## Database Migration

- **Flyway** is used for database schema migrations
- Chosen for its simplicity and suitability for small-to-medium sized projects

---

## Testing

- **Integration Tests**:
    - PostgreSQL 17 is used via **Testcontainers**
- **Test Strategy**:
    - Fake implementations are used where appropriate
    - Clear separation between domain and infrastructure tests
- **Test Execution**:
  ```bash
  mvn test
- A Postman collection is provided; it contains all available endpoints, along with two sample JSON request payloads.
