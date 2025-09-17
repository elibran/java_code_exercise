# Spring Boot Fundamentals — Personal Banking Management API

This repo is a **teachable, minimal** Spring Boot project that demonstrates the three‑tier architecture
(**Controller → Service → Repository**) while building a small Personal Banking REST API.

## Agenda Mapping

- **Introduction / Use Case:** A simple REST API to manage bank accounts
- **Three Tiers:** `AccountController`, `AccountService`, `AccountRepository`
- **JPA Basics:** `@Entity Account` + Spring Data JPA (`JpaRepository`)
- **Configuration:** `src/main/resources/application.properties`
- **Hands‑on:** Exercises included below
- **Conclusion / Key takeaways:** At the end of this doc

---

## 1) Prerequisites

- Java **17+**
- Maven **3.9+**
- An IDE (IntelliJ IDEA / VS Code) or just a terminal

## 2) Quickstart

```bash
# 1) Build & run
mvn spring-boot:run

# 2) Verify the app is up
curl http://localhost:8080/api/accounts/ping
```

## 3) Project Structure

```text
banking-app/
├── pom.xml
└── src
    ├── main
    │   ├── java/com/example/banking
    │   │   ├── BankingApplication.java
    │   │   ├── Account.java              # @Entity
    │   │   ├── AccountRepository.java    # extends JpaRepository
    │   │   ├── AccountService.java       # business logic
    │   │   └── AccountController.java    # REST endpoints
    │   └── resources
    │       └── application.properties    # H2 + JPA config
    └── test (optional)
```

## 4) Technology Stack

- Spring Boot
- Spring Data JPA
- H2 (in‑memory DB)
- Jakarta Validation (basic input validation)

## 5) Endpoints

- `GET /api/accounts/ping` — health check
- `POST /api/accounts` — create account
- `GET /api/accounts/{accountNumber}` — fetch account by number
- `PUT /api/accounts/{accountNumber}` — update `accountHolderName`

### Sample Requests

```bash
# Create account
curl -X POST http://localhost:8080/api/accounts   -H "Content-Type: application/json"   -d '{ "accountNumber": "AC-1001", "accountHolderName": "Alice", "balance": 500.0 }'

# Retrieve by account number
curl http://localhost:8080/api/accounts/AC-1001

# Update holder name
curl -X PUT http://localhost:8080/api/accounts/AC-1001   -H "Content-Type: application/json"   -d '{ "accountHolderName": "Alice Cooper" }'
```

## 6) H2 Console

- Visit `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:bankdb`
- User: `sa` (blank password)

## 7) Hands‑on Exercises

### Exercise 1 — Create Your First Controller

1. Open `AccountController` and review `@RestController` and `@RequestMapping("/api/accounts")`.
2. Add a new endpoint: `GET /api/accounts/count` that returns `accountRepository.count()` via the service.

### Exercise 2 — Create the Entity and Repository

1. Open `Account.java` and review `@Entity`, `@Id`, `@GeneratedValue`.
2. In `AccountRepository`, add a method `boolean existsByAccountNumber(String accountNumber);`
   and try it from the service.

### Exercise 3 — Configure the Database

1. Confirm the settings in `application.properties` (H2 URL, console path, `ddl-auto=update`).
2. Start the app and use the H2 console to inspect the `ACCOUNT` table.

## 8) Best Practices Highlighted

- **Separation of Concerns:** Controller (HTTP) vs Service (business) vs Repository (data).
- **Dependency Injection:** Constructor injection makes tests easier and dependencies explicit.
- **Convention over Configuration:** Spring Boot’s sensible defaults reduce boilerplate.
- **Spring Data JPA:** Leverage repository method naming (e.g., `findByAccountNumber`) to auto‑generate queries.
- **Validation:** `@Valid` + Jakarta validation annotations (e.g., `@NotBlank`, `@PositiveOrZero`).

## 9) Troubleshooting

- Port already in use → set a different port in `application.properties` (`server.port=8081`).
- H2 console not loading → ensure `spring.h2.console.enabled=true` and path is `/h2-console`.
- Entity errors → verify imports use `jakarta.persistence.*` (Spring Boot 3+).

---

## 10) Conclusion & Key Takeaways

- Controllers handle web traffic.
- Services contain business logic.
- Repositories manage persistence.
- Entities map objects to tables.
- `application.properties` keeps configuration out of code.

This layered architecture keeps the code **clean, maintainable, and easy to test**.
