
# Personal Banking – Spring Boot (Java) – Transactions & AOP

Demonstrates transactional flows with `@Transactional`, AOP-based logging,
global exception handling, and an external service call with timeouts.

## Quickstart

- Requirements: JDK 17+, Maven 3.9+
- Run:
  ```bash
  ./mvnw spring-boot:run
  # or: mvn spring-boot:run
  ```

H2 console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:bank`)

## Endpoints
- `POST /api/transfer` – JSON body: `{ "fromAccountId": 1, "toAccountId": 2, "amount": 50.00 }`
