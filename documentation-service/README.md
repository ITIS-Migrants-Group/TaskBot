# documentation-service

Microservice for storing, listing, searching and deleting text documents owned by Telegram users.

## Technologies

- Java 21
- Spring Boot 3.5.16 from the parent Maven POM
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Liquibase
- Lombok
- Maven multi-module build

## Responsibility

This service stores text only. It does not receive PDF, Word, image or binary files. Each row belongs to one Telegram user through `ownerId`; there is no JPA relation with User Service.

Logical model:

```text
document {
  id: UUID
  ownerId: Long
  content: String
  createdAt: timestamp
}
```

## Environment

| Variable | Default |
| --- | --- |
| `DOCUMENTATION_SERVICE_PORT` | `8092` |
| `DOCUMENTATION_SERVICE_DB_URL` | `jdbc:postgresql://documentation-db:5432/documentation_service_db` |
| `DATABASE_USER` | `taskbot` |
| `DATABASE_PASSWORD` | `taskbot` |

## Run locally

Start PostgreSQL:

```bash
docker compose up -d documentation-db
```

Run the service from the repository root:

```bash
mvn -pl documentation-service spring-boot:run
```

## Run with Docker

From `documentation-service`:

```bash
docker compose up --build
```

The service listens on `http://localhost:8092`.

## Liquibase

Liquibase runs on startup using:

```text
classpath:db/changelog/db.changelog-master.yaml
```

The initial migration creates `documents` with indexes on `owner_id` and `(owner_id, created_at)`. Hibernate is configured with `ddl-auto: validate`.

## Endpoints

### Create

```bash
curl -X POST http://localhost:8092/documents/20 \
  -H "Content-Type: application/json" \
  -d '{"content":"Mi nota importante"}'
```

### List by owner

```bash
curl http://localhost:8092/documents/20
```

### Search by owner

```bash
curl "http://localhost:8092/documents/20/search?query=importante"
```

### Delete

```bash
curl -X DELETE http://localhost:8092/documents/20/{documentId}
```

## API Gateway

No API Gateway module exists in this checkout. The route needed by the gateway is:

```text
/documents/** -> http://documentation-service:8092
```

For Docker Compose, the internal service URL is `http://documentation-service:8092`.

## Tests

From the repository root:

```bash
mvn clean test
```

For only this module:

```bash
mvn -pl documentation-service test
```
