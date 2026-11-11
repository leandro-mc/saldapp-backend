# SaldApp — Backend

> Leer en español: [README.es.md](README.es.md)

REST API for splitting group expenses. Create a group, add members and expenses, and SaldApp computes each member's balance and the **minimum set of payments** needed to settle all debts. Companion frontend: [saldapp-frontend](https://github.com/leandro-mc/saldapp-frontend).

## Features

- Groups, members (integrants) and expenses with full CRUD
- Per-member **balances** and a **settlement plan** (who pays whom, and how much)
- Group **closing**, after which it becomes read-only
- Consistent error responses through a global exception handler
- Interactive API docs with Swagger UI

## Tech stack

- Java 21, Spring Boot, Spring Data JPA
- PostgreSQL (Docker Compose), Maven
- Clean / hexagonal architecture: domain, use cases with input/output ports, web and persistence adapters
- JUnit tests for the settlement algorithm

## Getting started

Requirements: JDK 21+, Maven and Docker.

```bash
git clone https://github.com/leandro-mc/saldapp-backend.git
cd saldapp-backend
cp .env.example .env        # set DB credentials
docker compose up -d        # PostgreSQL on port 5435
mvn spring-boot:run         # API on http://localhost:8080
```

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Structure

```
src/main/java/com/lmora/saldapp/
├── domain/        # Pure models, exceptions and SettlementCalculator
├── application/   # Use cases and ports (in / out)
├── adapter/       # in/web (controllers, DTOs) and out/persistence (JPA)
└── infraestructure/  # Configuration (CORS)
```

## Highlight: settlement algorithm

`SettlementCalculator` splits members into debtors and creditors using two priority queues and repeatedly matches the largest debt with the largest credit. This keeps the number of transactions low, using `BigDecimal` for exact money handling.
