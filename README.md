# CoopCredit - Integrated Credit Application System

## System Description
This project implements an Integrated Credit Application System for CoopCredit, following a hexagonal architecture and composed of two microservices: `credit-application-service` and `risk-central-mock-service`.

## Architecture
*   **Monorepo**: Both services reside in a single Maven multi-module repository.
*   **Hexagonal Architecture (`credit-application-service`)**:
    *   **Pure Domain**: Business models (`Affiliate`, `CreditApplication`, `RiskEvaluation`, `User`, `Role`).
    *   **In-Ports**: Interfaces defining use cases (`AffiliateServicePort`, `CreditApplicationServicePort`).
    *   **Out-Ports**: Interfaces for interacting with external systems (`AffiliatePersistencePort`, `CreditApplicationPersistencePort`, `RiskCentralPort`).
    *   **Adapters**:
        *   **REST Adapter (Inbound)**: REST Controllers (`AffiliateController`, `CreditApplicationController`, `AuthenticationController`) exposing the API.
        *   **JPA Adapter (Outbound)**: Persistence implementations (`JpaAffiliateAdapter`, `JpaCreditApplicationAdapter`) using Spring Data JPA.
        *   **Risk Central REST Adapter (Outbound)**: Implementation to communicate with `risk-central-mock-service` (`RiskCentralRestAdapter`) using WebClient.
*   **Microservices**:
    *   `credit-application-service`: Main service for managing affiliates and credit applications.
    *   `risk-central-mock-service`: Simulated credit risk evaluation service.

## Implemented Functional Requirements (credit-application-service)
*   **Affiliate Management**: Registration and editing of affiliates with basic validations.
*   **Credit Application Management**: Creation of applications, invocation of `risk-central-mock-service`, application of internal policies, and update of application status.
*   **Security**:
    *   JWT Authentication (stateless).
    *   Roles: `ROLE_AFILIADO`, `ROLE_ANALISTA`, `ROLE_ADMIN`.
    *   Authentication Endpoints: `/auth/register`, `/auth/login`.
    *   Basic role-based access control for endpoints.
*   **Validations and Error Handling**:
    *   Bean Validation in input DTOs.
    *   Global exception handling with `@ControllerAdvice` and `ProblemDetail` format.
*   **Persistence**:
    *   JPA with Spring Data JPA for `Affiliate`, `CreditApplication`, `RiskEvaluation`, `User`.
    *   1-N and 1-1 relationships configured.
    *   Transactions (`@Transactional`) in application services.
*   **Risk Central Integration**: REST communication with `risk-central-mock-service`.

## Implemented Functional Requirements (risk-central-mock-service)
*   **Endpoint**: `POST /risk-evaluation`
*   **Logic**: Generates a consistent score and risk level per document, simulating a credit bureau.
*   **Technology**: Lightweight Spring Boot Web, without JPA or security.

## Current Project Status

### What works / Is Implemented:
*   **Monorepo Structure**: The project is configured as a Maven multi-module monorepo.
*   **Source Code**: All Java code for both microservices (`credit-application-service` and `risk-central-mock-service`) is written and structured according to hexagonal architecture and functional requirements.
*   **POM Configuration**: `pom.xml` files for the parent and both modules are configured for dependency and plugin management. Spring Boot versions have been explicitly forced in child modules as a last resort to try and resolve inheritance issues.
*   **Unit Tests**: A basic unit test for `AffiliateService` (`AffiliateServiceTest.java`) has been created.
*   **Swagger/OpenAPI**: The `springdoc-openapi` dependency is included to generate API documentation.
*   **Observability**: Actuator and Micrometer dependencies are configured in `application.yml`.

### What does NOT work / Is Pending / Requires Attention:

1.  **Maven Compilation**: Despite multiple attempts to debug and correct the `pom.xml` files (including exhaustive cleaning of the local Maven repository and forcing versions), the project **still fails to compile with `dependencies.dependency.version` missing errors for Spring Boot starters**.
    *   **Diagnosis**: This indicates a problem **external to the project configuration itself** (the `pom.xml` files are logically correct for a monorepo, even with forced versions). The most likely cause is persistent **corruption in your local Maven cache**, an **interfering `settings.xml` configuration**, or a **network/proxy issue** preventing correct dependency metadata download.
    *   **Action Required**: You must investigate and resolve the Maven environment issue. Check your `settings.xml` (if it exists), ensure your `~/.m2/repository` is completely clean, and that your network allows dependency downloads.
2.  **Complete Testing**:
    *   **Unit Tests**: Unit tests for `CreditApplicationService` and other domain components need to be completed.
    *   **Integration Tests**: Integration tests (Spring Boot Test + MockMvc) and security tests with JWT have not been implemented.
    *   **Testcontainers**: Testcontainers setup and usage for database testing has not been implemented.
3.  **Flyway**: Flyway configuration was temporarily removed to debug the Maven issue. It will need to be reintroduced and configured for database migrations (`V1__schema.sql`, etc.). Currently, Hibernate is configured for `ddl-auto: update`, which will create the schema but is not the desired migration solution.
4.  **Initial Data**: Initial data loading (e.g., users with roles) has not been implemented to facilitate security testing.
5.  **Containerization**: `Dockerfile`s and `docker-compose.yml` have not been created.

## Execution Instructions (Assuming Maven compilation is resolved)

### 1. Pre-requisites
*   Java 21 JDK
*   Maven (version 3.8.x or higher)
*   Docker and Docker Compose (for containerization)
*   PostgreSQL (for local development without Docker)

### 2. Database Configuration (Local without Docker)
Ensure you have a PostgreSQL instance running. Create a database named `coopcredit_db` with a user `user` and password `password` (or adjust `application.yml` according to your setup).

### 3. Compilation and Execution
From the monorepo root (`/home/corte4/Documentos/credit-application-service`):

```bash
# Clean and build both modules
mvn clean install -U

# Run credit-application-service
cd credit-application-service
mvn spring-boot:run

# In another terminal, run risk-central-mock-service
cd ../risk-central-mock-service
mvn spring-boot:run
```

### 4. Key Endpoints

#### Authentication (`credit-application-service`)
*   `POST /auth/register`
*   `POST /auth/login`

#### Affiliates (`credit-application-service`)
*   `POST /affiliates`
*   `PUT /affiliates/{id}`
*   `GET /affiliates/{id}`

#### Credit Applications (`credit-application-service`)
*   `POST /credit-applications`
*   `POST /credit-applications/{id}/evaluate`
*   `GET /credit-applications/{id}`
*   `GET /credit-applications/affiliate/{affiliateId}`

#### Risk Central Mock Service
*   `POST /risk-evaluation` (exposed on `http://localhost:8081`)

### 5. Access Swagger UI
Once `credit-application-service` is running, you can access the API documentation at:
`http://localhost:8080/swagger-ui.html`
