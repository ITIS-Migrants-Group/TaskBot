[![документация](https://img.shields.io/badge/doc-документация-blue)](Documentation.md)

# TaskBot
Проект телеграм бот ассистент. Задача летней практики от ИТИС 2026 в Парнас АйТи.

## Docker Compose

The root `compose.yaml` starts the services that are present in this checkout:

- `user-service` on `${USER_SERVICE_PORT:-8090}`
- `todo-service` on `${TODO_SERVICE_PORT:-8091}`
- `documentation-service` on `${DOCUMENTATION_SERVICE_PORT:-8092}`
- `notification-service` on `${NOTIFICATION_SERVICE_PORT:-8093}`
- RabbitMQ management UI on `15672`

Create a local `.env` from `.env.example` and run:

```bash
docker compose up --build
```

`contact-service`, `api-gateway` and `bot` variables are reserved in `.env.example`, but their modules are not present in this repository snapshot.
