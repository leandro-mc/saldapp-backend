# Decisiones pendientes — SaldApp Backend

> Documento de trabajo. Resume el análisis por capas y deja **cada decisión abierta** para tomarla en una etapa posterior. Nada de lo que aparece como "pendiente" se ha aplicado.

## Estado de esta etapa

| Tema | Resultado |
|------|-----------|
| Rama de control | `dev` (ya existía; se sincronizó con `main`). La rama por defecto sigue siendo `main`. |
| Credenciales en código e historial | Revisados el árbol y las 94 commits de todas las ramas con gitleaks y un barrido propio. **No hay credenciales reales.** Solo existe un valor por defecto débil de contraseña de base de datos en `docker-compose.yml` (ver D-08). |
| Ramas existentes | `dev`, cinco ramas `feat/*` y `main`; todas las `feat/*` ya están fusionadas (ver D-10). |

## Resumen por capas

| Capa | Estado actual | Observación principal |
|------|---------------|-----------------------|
| Base de datos | PostgreSQL 16 (Docker Compose, puerto 5435, con `healthcheck` y volumen) | El esquema lo genera Hibernate con `ddl-auto: update`; no hay migraciones ni script SQL. |
| Backend | Java 21, Spring Boot 4, JPA, arquitectura hexagonal, Swagger UI | 14 pruebas (13 del algoritmo de liquidación y una de contexto); sin autenticación. |
| Frontend | Repositorio aparte: [saldapp-frontend](https://github.com/leandro-mc/saldapp-frontend) | La API se consume por REST; el origen permitido se configura con `CORS_ALLOWED_ORIGINS`. |
| Infraestructura | `docker-compose.yml` solo levanta la base de datos | No hay Dockerfile de la API ni integración continua. |

## Decisiones

### D-01 · Base de datos: migraciones
- **Contexto:** `ddl-auto: update` modifica el esquema al arrancar y no deja registro de cambios; dos máquinas pueden terminar con esquemas distintos.
- **Opciones:** (A) Flyway con una migración `V1` que reproduzca el esquema actual y `ddl-auto: validate`; (B) mantener `update` y documentarlo como decisión de MVP; (C) un `schema.sql` manual.
- **Recomendación:** A. Es el cambio de mayor valor para el portafolio en esta capa y el esfuerzo es bajo.
- **Esfuerzo:** bajo. **Decisión:** ☐ pendiente

### D-02 · Base de datos: datos de demostración
- **Contexto:** quien clone el proyecto parte de una base vacía.
- **Opciones:** (A) script o migración opcional con un grupo de ejemplo (integrantes, gastos y un cierre); (B) dejarlo vacío y documentar los pasos en el README.
- **Recomendación:** A, activable con un perfil `demo`, para que la pantalla de liquidación sea visible desde el primer arranque.
- **Esfuerzo:** bajo. **Decisión:** ☐ pendiente

### D-03 · Backend: pruebas
- **Contexto:** solo está probado el algoritmo (`SettlementCalculator`); casos de uso y endpoints no tienen pruebas.
- **Opciones:** (A) pruebas de casos de uso (grupo cerrado, integrante con gastos) más `@WebMvcTest` por controller; (B) añadir Testcontainers con PostgreSQL para pruebas de repositorios; (C) ambas.
- **Recomendación:** A primero; B si se adopta Flyway (D-01), porque valida las migraciones contra una base real.
- **Esfuerzo:** medio. **Decisión:** ☐ pendiente

### D-04 · Backend: autenticación y acceso
- **Contexto:** no hay Spring Security; quien conozca el id de un grupo puede leerlo y editarlo.
- **Opciones:** (A) mantenerlo abierto y documentar que es un MVP de demostración; (B) usar identificadores no adivinables (UUID) como mitigación sin usuarios; (C) añadir cuentas de usuario con JWT y pertenencia a grupos.
- **Recomendación:** A o B para el portafolio inmediato; C es un cambio de producto grande y conviene tratarlo como una rama aparte si se decide.
- **Esfuerzo:** A bajo, B medio, C alto. **Decisión:** ☐ pendiente

### D-05 · Backend: higiene del código
- **Contexto:** typos en paquetes (`dto/expese`, `infraestructure`), nombres inconsistentes (`GroupFinancialUseCaseImp` frente a `...Impl`), metadatos vacíos en el `pom.xml` y revisión de `@Valid`/paginación en los listados.
- **Opciones:** (A) corregir todo en una sola rama; (B) corregir solo los nombres visibles y dejar el resto.
- **Recomendación:** A. Son cambios mecánicos y conviene hacerlos antes de que el proyecto crezca.
- **Esfuerzo:** bajo. **Decisión:** ☐ pendiente

### D-06 · Infraestructura: contenedor de la API
- **Contexto:** el compose actual solo levanta PostgreSQL; la API se ejecuta con Maven en la máquina local.
- **Opciones:** (A) Dockerfile multi-etapa (compilación y ejecución) y servicio `api` en el compose, con espera al `healthcheck` de la base; (B) dejarlo como está.
- **Recomendación:** A. Reduce el arranque a `docker compose up --build`.
- **Esfuerzo:** bajo. **Decisión:** ☐ pendiente

### D-07 · Infraestructura: ¿arranque conjunto con el frontend? (unificar o no)
Esta decisión **no es obligatoria**: solo se aplica si mejora el proyecto.

| Criterio | Evaluación |
|----------|------------|
| Acoplamiento | Medio. Dos repos, un contrato REST, sin código compartido, desarrollados a la vez. |
| Quién lo clona | Un revisor quiere ver la aplicación funcionando con un solo comando. |
| Coste de unir | Cambia la URL pública, hay que fusionar dos historiales y los dos repos tienen ramas `feat/*` paralelas. |

- **Opciones:** (A) monorepo (`backend/`, `frontend/`, `docker-compose.yml`) conservando el historial de ambos; (B) mantener los repos separados y que el compose del backend construya el frontend desde su URL de Git (Docker Compose admite una URL de Git como contexto de build); (C) mantener ambos separados, cada uno con su propio arranque y README.
- **Recomendación:** B. Da el arranque de un solo comando sin cambiar URLs ni historiales y es reversible. A solo si se quiere una única entrada de portafolio; C si el frontend se despliega por separado.
- **Esfuerzo:** B bajo, A medio-alto. **Decisión:** ☐ pendiente

### D-08 · Infraestructura: contraseña por defecto de la base
- **Contexto:** `docker-compose.yml` define un valor por defecto para `POSTGRES_PASSWORD` si no se establece `DB_PASSWORD`. No es un secreto real, pero está en un repositorio público.
- **Opciones:** (A) quitar el valor por defecto para que el compose falle si falta la variable; (B) conservarlo y documentar que es solo para desarrollo local.
- **Recomendación:** A, sumado a un `.env.example` que ya existe.
- **Esfuerzo:** bajo. **Decisión:** ☐ pendiente

### D-09 · Documentación técnica
- **Opciones:** exportar la especificación OpenAPI al repositorio, añadir un diagrama de la arquitectura hexagonal y ejemplos `curl` del flujo principal (crear grupo, añadir gasto, cerrar, ver liquidación).
- **Recomendación:** hacerlo después de D-01 a D-06, cuando la estructura sea estable.
- **Esfuerzo:** bajo. **Decisión:** ☐ pendiente

### D-10 · Control de versiones y CI
- **Ramas:** las cinco ramas `feat/*` (`expense`, `group`, `integrant`, `model-enhancements`, `settlement`) no contienen commits que `main` no tenga, así que se pueden borrar sin perder nada. La propuesta es dejar solo `main` y `dev`.
- **CI:** flujo de GitHub Actions que compile y ejecute las pruebas en cada `push` y `pull request`, con insignia en el README.
- **Recomendación:** limpiar ramas y añadir CI en la misma rama.
- **Esfuerzo:** bajo. **Decisión:** ☐ pendiente

## Ramas de trabajo propuestas

Para no llenar el repositorio de ramas, se agrupan las tareas en pocas ramas desde `dev`:

1. `chore/database-and-quality`: D-01, D-02, D-03, D-05.
2. `feat/containers-and-ci`: D-06, D-07, D-08, D-10.
3. Rama aparte solo si se elige la opción C de D-04.

## Orden sugerido

D-01 → D-05 → D-03 → D-06 → D-08 → D-10 → D-07 → D-02 → D-09 (D-04 según decisión).
