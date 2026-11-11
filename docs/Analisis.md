# Análisis técnico — SaldApp Backend

Documento de apoyo para futuras mejoras. No forma parte de la documentación principal.

## Qué es y cómo está construido

API Spring Boot 4 / Java 21 de ~1.800 líneas con PostgreSQL. Cuatro controllers (grupos, integrantes, gastos, finanzas) sobre casos de uso, con mappers explícitos entre capas.

## Arquitectura

Arquitectura hexagonal bien aplicada, es lo más sólido del proyecto:

- `domain/` sin anotaciones de framework (ni `@Entity`), con la lógica compleja aislada en `SettlementCalculator`.
- `application/port/in|out` define casos de uso y repositorios como interfaces.
- `adapter/in/web` y `adapter/out/persistence` traducen con DTOs y mappers propios (modelo de dominio y entidad JPA separados).

El coste es la cantidad de mappers y clases por entidad, razonable para un proyecto que busca demostrar el patrón.

## Oportunidades principales

1. **Pruebas de capas superiores.** Hay 13 tests del algoritmo de liquidación y el test de contexto, pero ninguno para los casos de uso (reglas como grupo cerrado, integrante con gastos) ni de integración de controllers/repositorios (`@WebMvcTest`, Testcontainers).
2. **Migraciones de base de datos.** Se usa `ddl-auto: update`, que modifica el esquema automáticamente y no es reproducible. Adoptar Flyway o Liquibase.
3. **Autenticación.** No hay usuarios ni seguridad: cualquiera que conozca el id de un grupo puede leerlo y editarlo. Es un cambio de diseño relevante (identidad, pertenencia a grupos).
4. **Validación y paginación.** Revisar validaciones de entrada (`@Valid`) en todos los requests y paginar los listados si crecen.

## Prácticas a revisar

- **Typos en nombres de paquetes:** `dto/expese` y `infraestructure`. Corregirlos pronto evita arrastrar el error.
- **Nombres de clases inconsistentes:** `GroupFinancialUseCaseImp` frente a `...UseCaseImpl` en el resto.
- **Dependencias del `pom.xml`:** hay metadatos vacíos (`<name/>`, `<licenses>`, `<developers>`) que conviene completar o eliminar.
- **Credenciales por defecto** en `docker-compose.yml` (`postgres/postgres`) y `.env.example`: válidas para desarrollo local, no para otros entornos.
- Falta CI (compilar y correr tests en cada push) y un Dockerfile para la propia API (el compose solo levanta la base de datos).
