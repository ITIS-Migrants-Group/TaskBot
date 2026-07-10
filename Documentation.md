## Документация
### Компоненты проекта
### TgBot
Клиентское приложение в виде телеграм бота
### API Gateway
Точка входа с для клиента.

### User Service
Управление пользователями системы.

Бизнес требования: создание, удаление аккаунта.
#### Схема данных
```text
account 
{
    tg-id: long,
    username: string
}
```
### Endpoints
```http request
GET /users/{tg-id}

returns
{
"tg-id": 20,
"username": "test"
}
```
```http request
POST /users/{tg-id}

returns
{
"tg-id": 20,
"username": "test"
}
```
```http request
DELETE /users/{tg-id}

returns
{
"tg-id": 20,
"username": "test"
}
```
### Todo Service
Сервис управления списками и задачами.

Бизнес требования: создание, завершение и удаление задач, просмотр списка задач по статусу и дате.
#### Схема данных
```text
task
{
   id: uuid,
   userId: long
   title: string,
   created_at: timestamp,
   ended_at: timestamp
   status: string
}
status {
    NEW, COMPLETED
}
```
#### Endpoints
```http request
GET /tasks/{tg-id}?status={}&ended_at={}

returns
[
{
    "id": "abracadabraUUID",
    "userId": 20,
    "title": "task #1",
    "at_created": "2026-07-10T10:26:30+04:00",
    "at_ended": "2026-07-15T12:00:00+04:00",
    "status": "NEW"
}
]
```
```http request
GET /tasks/{tg-id}/search?query={}

returns
[
{
    "id": "abracadabraUUID",
    "userId": 20,
    "title": "task #1",
    "at_created": "2026-07-10T10:26:30+04:00",
    "at_ended": "2026-07-15T12:00:00+04:00",
    "status": "NEW"
}
]
```
```http request
POST /tasks/{tg-id}

{
    "title": "task #1",
    "deadline": "2026-07-15T12:00:00+04:00"
}

returns
{
    "id": "abracadabraUUID",
    "userId": 20,
    "title": "task #1",
    "at_created": "2026-07-10T10:26:30+04:00",
    "at_ended": "2026-07-15T12:00:00+04:00",
    "status": "NEW"
}
```
```http request
PUT /tasks/{tg-id}/{task-id}

{
    "status": "COMPLETED"
}

returns
{
    "id": "abracadabraUUID",
    "userId": 20,
    "title": "task #1",
    "created_at": "2026-07-10T10:26:30+04:00",
    "ended_at": "2026-07-15T12:00:00+04:00",
    "status": "COMPLETED"
}
```
```http request
DELETE /tasks/{tg-id}/{id}

returns
{
    "id": "abracadabraUUID",
    "userId": 20,
    "title": "task #1",
    "created_at": "2026-07-10T10:26:30+04:00",
    "ended_at": "2026-07-15T12:00:00+04:00",
    "status": "COMPLETED"
}
```
### Contact Service
Сервис управления контактами.

Бизнес требования: добавление, редактирование и удаление контактов.
#### Схема данных
```text
contact
{
    id: uuid,
    ownerId: long,
    name: string,
    phoneNumber: string,
    email: string,
    company: string,
    note: string
}
```
#### Endpoints
```http request
GET /contacts

returns
[
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}
]
```
```http request
GET /contacts/search?query={}

returns
[
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}
]
```
### Documentation Service
Хранение и поиск документов и информации.

Бизнес требования: сохранение текстового описания.

#### Схема данных
```text
document
{
    id: uuid,
    ownerId: long,
    content: string,
    created_at: timestamp
}
```
#### Endpoint
```http request
GET /documents/search?query={}

returns
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "content": "hehe",
    "created_at": "2026-07-10T10:26:30+04:00"
}
```
```http request
POST /document/{tg-id}

{
    "content": "heheh"
}

returns
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "content": "hehe",
    "created_at": "2026-07-10T10:26:30+04:00"
}
```
```http request
DELETE /document/{tg-id}/{id}

returns
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "content": "hehe",
    "created_at": "2026-07-10T10:26:30+04:00"
}
```
### Notification Service
Сервис управления напоминаниями и уведомлениями.

Бизнес требования: создание и разовых и периодических уведомлений, уведомления о приближающихся дедлайнах.
#### Cхема данных
```text
notification
{
    id: uuid,
    ownerId: long,
    title: string
    taskId: uuid,
    type: string,
    notify_at: timestamp,
    period: interval,
    is_active: boolean
}

notification_type {
    ONCE, PERIOD
}
```
#### Endpoint
```http request
POST /notifications/{tg-id}

{
    "title": "notify me",
    "type": "ONCE",
    "notify_at": "2026-07-10T10:26:30+04:00",
    "period": null
}

retunrs
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "title": "notify me"
    "taskId": abrakadabraUUID2,
    "type": "ONCE",
    "notify_at": "2026-07-10T10:26:30+04:00",
    "period": null,
    "is_active": true
}
```