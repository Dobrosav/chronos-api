# Chronos API ⌚

A wristwatch collection management application.
Built with the **Quarkus** framework ("Supersonic Subatomic Java") using a **Package by Feature** architecture.

## Technologies

- **Java 21+**
- **Quarkus** (REST, Jackson)
- **Hibernate ORM with Panache** (Active Record pattern)
- **PostgreSQL** database
- **REST Assured** (Integration testing)
- **Docker Compose** (Local environment)

## Architecture (Package by Feature)

The application is organized by business domains (features) rather than technical layers.

```text
src/main/java/io/dobrosav/
├── brand/               # Watch brands domain
│   ├── Brand.java            (Panache Entity)
│   ├── BrandDto.java         (Data Transfer Object)
│   ├── BrandService.java     (Business logic & logging)
│   └── BrandResource.java    (REST Endpoints)
├── model/               # Watch models domain
│   ├── WatchModel.java       (Panache Entity)
│   ├── WatchModelDto.java    (Data Transfer Object)
│   ├── WatchModelService.java (Business logic & logging)
│   └── WatchModelResource.java (REST Endpoints)
├── collection/          # Individual watch items domain
│   ├── WatchItem.java        (Panache Entity)
│   ├── WatchItemDto.java     (Data Transfer Object)
│   ├── WatchItemService.java (Business logic & logging)
│   └── WatchItemResource.java (REST Endpoints)
└── common/              # Shared exception handlers and utilities
    └── GlobalExceptionHandler.java
```

## Configuration (`.env`)

Database credentials are not stored in the repository. They are read from a `.env` file in the project root, which is ignored by git.

Create it from the template:
```bash
cp .env.example .env
```

| Variable | Description | Example |
|---|---|---|
| `POSTGRES_DB` | Database name | `chronos` |
| `POSTGRES_USER` | Database user | `postgres` |
| `POSTGRES_PASSWORD` | Database password | `change-me` |

The same file is used by:
- **Docker Compose**: loaded automatically and used for both `db` and `api` services. Compose fails fast if a variable is missing.
- **Quarkus dev mode**: Quarkus automatically loads `.env` from the project root (see `application.properties`).

> **Note:** Postgres sets the password only on the first start with an empty volume. If you change `POSTGRES_PASSWORD` later, either recreate the volume (`docker compose down -v`, **deletes data**) or change it in the database with `ALTER USER`.

## Running the Application (Dev Mode)

The application relies on a PostgreSQL database. The database is defined in `docker-compose.yml`.

1. **Start only the database:**
   ```bash
   docker compose up -d db
   ```
   *(Starting only `db` matters, because the `api` container would otherwise occupy port 8080.)*

2. **Start Quarkus in dev mode:**
   ```bash
   ./mvnw quarkus:dev
   ```
   *Note:* Upon application startup, database schema updates are automatically applied via `quarkus.hibernate-orm.schema-management.strategy=update`.

3. **Quarkus Dev UI:** Available at <http://localhost:8080/q/dev/>

## Running with Docker (Native Image)

The whole stack (database + API) can be started with Docker Compose. The `Dockerfile` is a multi-stage build:

- **Build stage:** `ubi9-quarkus-mandrel-builder-image:jdk-25` compiles the application into a native executable (Quarkus 3.40 requires Mandrel 25).
- **Runtime stage:** `ubi9-quarkus-micro-image:2.0`, a minimal UBI9 image. It must be UBI9 to match the glibc version of the builder.

```bash
docker compose up -d --build   # build and start
docker compose logs -f api     # follow API logs
docker compose down            # stop (data is kept in the postgres_data volume)
```

The API is available at <http://localhost:8080>.

> **Note:** The native build takes a few minutes and needs a lot of memory (~6 GB peak). On WSL2, make sure enough memory is assigned in `.wslconfig`. Classes that are serialized to JSON but not returned directly from resource methods (e.g. the error DTO in exception mappers) must be annotated with `@RegisterForReflection`.

## Testing

The application uses Quarkus Dev Services for testing. When tests are executed, Quarkus automatically spins up a clean Postgres container via Testcontainers in Docker.

Run all tests with:
```bash
./mvnw test
```

## REST API Endpoints

### Brands (`/brands`)
- `GET /brands?page=0&size=10` - List brands with pagination
- `GET /brands/{id}` - Get a single brand by ID
- `POST /brands` - Create a new brand (returns `409 Conflict` if brand name exists)
- `PUT /brands/{id}` - Update an existing brand
- `DELETE /brands/{id}` - Delete a brand (`204 No Content`)

### Watch Models (`/watch-models`)
- `GET /watch-models` - List all watch models
- `GET /watch-models/{id}` - Get a single watch model by ID
- `POST /watch-models` - Create a new watch model (returns `409 Conflict` if `referenceNumber` exists, `404 Not Found` if `brandId` is invalid)
- `DELETE /watch-models/{id}` - Delete a watch model (`204 No Content`)

### Watch Collection Items (`/watch-items`)
- `GET /watch-items` - List all individual watch items in the collection
- `GET /watch-items/{id}` - Get a single watch item by ID
- `POST /watch-items` - Add a watch item to the collection (`201 Created`, requires valid `watchModelId`)
- `DELETE /watch-items/{id}` - Delete a watch item from the collection (`204 No Content`)
