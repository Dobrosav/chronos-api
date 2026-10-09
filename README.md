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
├── brand/               # Everything related to watch brands
│   ├── Brand.java       (Panache Entity)
│   ├── BrandDto.java    (Data Transfer Object)
│   ├── BrandService.java (Business logic)
│   └── BrandResource.java (REST Endpoints)
├── watch/               # Everything related to individual watches (COMING SOON)
└── common/              # Shared code, configurations, errors
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
   *Note:* Upon the first application startup, an empty table will be automatically created thanks to `hibernate-orm.schema-management.strategy=update`.

3. **Seed the database (Optional):**
   In another terminal, run the SQL script to insert 15 popular brands:
   ```bash
   docker exec -i chronos-db psql -U postgres -d chronos < db/seed.sql
   ```

4. **Quarkus Dev UI:** Available at <http://localhost:8080/q/dev/>

## Testing

The application uses Quarkus Dev Services for testing. When tests are executed, Quarkus automatically spins up a *clean* Postgres container in Docker (leaving your development database untouched).

Run the tests with:
```bash
./mvnw test
```

## REST API (Brands)

- `GET /brands` - List all brands
- `GET /brands/{id}` - Get a single brand
- `POST /brands` - Create a new brand (returns 409 if name exists)
- `PUT /brands/{id}` - Update an existing brand
