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

## Running the Application (Dev Mode)

The application relies on a PostgreSQL database. The database is defined in `docker-compose.yml`.

1. **Start the database:**
   ```bash
   docker compose up -d
   ```

2. **Start Quarkus in dev mode:**
   ```bash
   ./mvnw quarkus:dev
   ```
   *Note:* Upon application startup, database schema updates are automatically applied via `quarkus.hibernate-orm.schema-management.strategy=update`.

3. **Seed the database (Optional):**
   In another terminal, run the SQL script to insert 15 popular brands:
   ```bash
   docker exec -i chronos-db psql -U postgres -d chronos < db/seed.sql
   ```

4. **Quarkus Dev UI:** Available at <http://localhost:8080/q/dev/>

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
