# SaldApp — Backend

> Read in English: [README.md](README.md)

API REST para dividir gastos grupales. Creas un grupo, agregas integrantes y gastos, y SaldApp calcula el balance de cada integrante y el **mínimo de pagos** necesarios para saldar todas las deudas. Frontend complementario: [saldapp-frontend](https://github.com/leandro-mc/saldapp-frontend).

## Funciones

- Grupos, integrantes y gastos con CRUD completo
- **Balances** por integrante y **plan de liquidación** (quién le paga a quién y cuánto)
- **Cierre** de grupo, tras el cual queda en solo lectura
- Respuestas de error consistentes mediante un manejador global de excepciones
- Documentación interactiva con Swagger UI

## Tecnologías

- Java 21, Spring Boot, Spring Data JPA
- PostgreSQL (Docker Compose), Maven
- Arquitectura limpia / hexagonal: dominio, casos de uso con puertos de entrada y salida, adaptadores web y de persistencia
- Pruebas JUnit del algoritmo de liquidación

## Cómo ejecutarlo

Requisitos: JDK 21+, Maven y Docker.

```bash
git clone https://github.com/leandro-mc/saldapp-backend.git
cd saldapp-backend
cp .env.example .env        # configura las credenciales de la BD
docker compose up -d        # PostgreSQL en el puerto 5435
mvn spring-boot:run         # API en http://localhost:8080
```

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Estructura

```
src/main/java/com/lmora/saldapp/
├── domain/        # Modelos puros, excepciones y SettlementCalculator
├── application/   # Casos de uso y puertos (in / out)
├── adapter/       # in/web (controllers, DTOs) y out/persistence (JPA)
└── infraestructure/  # Configuración (CORS)
```

## Destacado: algoritmo de liquidación

`SettlementCalculator` separa a los integrantes en deudores y acreedores con dos colas de prioridad y empareja repetidamente la mayor deuda con el mayor crédito. Así se mantiene bajo el número de transacciones, usando `BigDecimal` para manejar dinero con exactitud.
