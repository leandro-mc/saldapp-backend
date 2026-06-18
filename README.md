# SaldApp — Backend

Smart splitting of group expenses.

README also available in:

- [Spanish](README.es.md)

---

## Stack

| Element               | Choice              |
| --------------------- | ------------------- |
| Language / Framework  | Java + Spring Boot  |
| Database              | PostgreSQL (Docker) |
| Build                 | Maven               |
| Architecture          | Clean Architecture  |

---

## Prerequisites

- JDK 21+
- Docker and Docker Compose

---

## Run the project

**1. Clone the repository**

```bash
git clone https://github.com/leandro-mc/saldapp-backend.git
cd saldapp-backend
```

**2. Create the `.env` file** from the example:

```bash
cp .env.example .env
```

**3. Start the database**

```bash
docker compose up -d
```

**4. Run the application**

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## API Documentation

Once the application is running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Project structure

```
src/main/java/com/saldapp/
│
├── domain/
│   ├── model/          # Pure domain entities (no @Entity)
│   ├── exception/      # Domain exceptions
│   └── service/        # Complex business logic (settlement algorithm)
│
├── application/
│   ├── port/
│   │   ├── in/         # Use case interfaces (input ports)
│   │   └── out/        # Repository interfaces (output ports)
│   └── usecase/        # Use case implementations
│
├── adapter/
│   ├── in/
│   │   └── web/        # REST controllers
│   │       ├── dto/    # Request and Response
│   │       ├── mapper/
│   │       └── exception/ # Global error handling
│   └── out/
│       └── persistence/
│           ├── entity/     # JPA entities
│           ├── mapper/
│           ├── repository/ # JPA interfaces
│           └── adapter/    # Output port implementations
│
└── infrastructure/
    └── config/         # Spring configuration (beans, properties)
```

---

## Environment variables

Create a `.env` file in the project root based on `.env.example`:

```env
SPRING_PROFILES_ACTIVE=dev
DB_URL=jdbc:postgresql://localhost:5435/saldapp
DB_USERNAME=
DB_PASSWORD=
APP_PORT=8080
```

---

## Author

**Leandro Mora Corrales**  
[linkedin.com/in/leandromora](https://linkedin.com/in/leandromora)

---

## License

MIT License — see [LICENSE](LICENSE) for details.
