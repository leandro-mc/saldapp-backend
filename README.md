# SaldApp — Backend

División inteligente de gastos grupales.

---

## Stack

| Elemento | Elección            |
|---|---------------------|
| Lenguaje / Framework | Java + Spring Boot  |
| Base de datos | PostgreSQL (Docker) |
| Build | Maven               |
| Arquitectura | Clean Architecture  |

---

## Requisitos previos

- JDK 21+
- Docker y Docker Compose

---

## Levantar el proyecto

**1. Clonar el repositorio**
```bash
git clone https://github.com/leandro-mc/saldapp-backend.git
cd saldapp-backend
```

**2. Crear el archivo `.env`** a partir del ejemplo:
```bash
cp .env.example .env
```

**3. Levantar la base de datos**
```bash
docker compose up -d
```

**4. Correr la aplicación**
```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## API Documentation

Once the app is running, Swagger UI is available at:
http://localhost:8080/swagger-ui/index.html

---

## Estructura del proyecto

```
src/main/kotlin/com/saldapp/
│
├── domain/
│   ├── model/          # Entidades de dominio puras (sin @Entity)
│   ├── exception/      # Excepciones de dominio
│   └── service/        # Lógica de negocio compleja (algoritmo de settlement)
│
├── application/
│   ├── port/
│   │   ├── in/         # Interfaces de casos de uso (puertos de entrada)
│   │   └── out/        # Interfaces de repositorios (puertos de salida)
│   └── usecase/        # Implementación de los casos de uso
│
├── adapter/
│   ├── in/
│   │   └── web/        # Controllers REST
│   │       ├── dto/    # Request y Response
│   │       ├── mapper/
│   │       └── exception/ # Manejo global de errores
│   └── out/
│       └── persistence/
│           ├── entity/      # Entidades JPA
│           ├── mapper/
│           ├── repository/  # Interfaces JPA
│           └── adapter/     # Implementación de los puertos de salida
│
└── infrastructure/
    └── config/         # Configuración de Spring (beans, properties)
```

---

## Variables de entorno

Crear un `.env` en la raíz del proyecto basado en `.env.example`:

```env
SPRING_PROFILES_ACTIVE=dev
DB_URL=jdbc:postgresql://localhost:5435/saldapp
DB_USERNAME=
DB_PASSWORD=
APP_PORT=8080
```

## Autor

**Leandro Mora Corrales**
[linkedin.com/in/leandromora](https://linkedin.com/in/leandromora)

## Licencia

MIT License — see [LICENSE](LICENSE) for details.
