## Документация
### Компоненты проекта
### TgBot
Клиентское приложение в виде телеграм бота

|   КОМАНДА    |                     МЕТОД API                     |                                                             ПРИМЕЧАНИЕ                                                             |
|:------------:|:-------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------:|
|   `/start`   |              `POST /api/v1/register`              |                                сначала получить пользователя по чату, если его нет зарегистрировать                                |
| `/task new`  |         `POST /api/v1/tasks/{tg-id}`              |                                         после вызова бот дает ввести необходимые параметры                                         |
| `/task list` | `GET /api/v1/tasks/{tg-id}?status={}&ended_at={}` | после вызова выдает выбрать один из статусов NEW/COMPLETED и время дедлайна, список задач выводится с кнопками ЗАВЕРШИТЬ и УДАЛИТЬ |
|      ``      |                                                   |                                                                                                                                    |

### API Gateway
Точка входа с для клиента.

- получить пользователя
```http request
GET /api/v1/users/{tg-id}

returns
{
"tgId": 20,
"username": "test"
}
```
- при активации бота регистрируем пользователя в системе
```http request
POST /api/v1/users

{
    "tgId": 20,
    "username": "vasya_pupkin"
}
```

- выдаем пользователю список задач с 2мя фильтрами
```http request

GET /api/v1/tasks/{tg-id}?status={}&ended_at={}

{
    "id": "abracadabraUUID",
    "userId": 20,
    "title": "task #1",
    "at_created": "2026-07-10T10:26:30+04:00",
    "at_ended": "2026-07-15T12:00:00+04:00",
    "status": "NEW"
}
```
- создаем новую задачу + добавить проверка что deadline >= now()
```http request
POST /api/v1/tasks/{tg-id}

{
    "title": "task #1",
    "deadline": "2026-07-15T12:00:00+04:00",
    "notify_for": сюда передается ISO 8601 как пример PT30M (30 минут) 
    в боте создадим enum с такими это время за которое нужно напомнит, то есть 
    если PT30M то напоминаем за 30 минут до дедлайна, это означает что время notify_at(POST /notifications/{tg-id})
    нужно уменьшить на этот параметр
}

returns CREATED
{
    "id": "abracadabraUUID",
    "userId": 20,
    "title": "task #1",
    "at_created": "2026-07-10T10:26:30+04:00",
    "at_ended": "2026-07-15T12:00:00+04:00",
    "status": "NEW"
}
```
**Примечание**: после создания таски создать нотификацию
```http request
POST /notifications/{tg-id}

{
    "title": текст сделать стандартным Задача: {title задачи},
    "type": тип обязательно ONCE,
    "notify_at": "2026-07-10T10:26:30+04:00",
    "period": null
}
```
- завершение задачи 
```http request
PUT /api/v1/notification/{id}
```

- удаляем задачу
```http request
DELETE /api/v1/tasks/{tg-id}/{id}

returns NO_CONTENT
```
**Примечание**: после удаления таски выключить нотификацию
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

- изменение статуса задачи
```http request
PUT /api/v1/tasks/{tg-id}/{task-id}

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
**Примечание**: если задача в статусе COMPLETED выключить уведомление
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

- добавить новое уведомление
```http request

POST /api/v1/notifications/{tg-id}

{
    "title": "notify me",
    "taskId": "abrakadabraUUID2",
    "type": "ONCE",
    "notify_at": "2026-07-10T10:26:30+04:00",
    "period": null
}

returns
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
**Примечание**: проверять что notify_at >= now(), а также если type=PERIOD, то period != null

- выключать уведомления
```http request
DELETE /api/v1/notifications/{tg-id}/{id}
```
**Примечание**: уведомления можно просто отключить не удаляя из бд
```http request

PUT /api/v1/notifications

{
    "id": "abracadabraUUID",
    "isActive": true
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

- добавить документ
```http request
POST /api/v1/documents/{tg-id}

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

- удалить документ
```http request
DELETE /api/v1/document/{tg-id}/{id}

returns
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "content": "hehe",
    "created_at": "2026-07-10T10:26:30+04:00"
}
```
- создать новый контакт
```http request
POST /api/v1/contacts/{tg-id}

{
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}

returns
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}
```

- изменить контакт
```http request
PUT /api/v1/contacts/{tg-id}/

{
    "id": "abrakadabraUUID",
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}

returns
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}
```
- удалить контакт
```http request
DELETE /api/v1/contacts/{tg-id}/{id}

returns NO_CONTENT
```

- поиск по контактам, таскам и документам
```http request
GET /api/v1/search/query={}

returns

{
    "items": [
    {
        "type": {"CONTACT/DOCUMENT/TASK"},
        "id": "abracadabraUUID",
        "title": {имя контакта, контент документа, тайтл задачи}
    },
    {
        "type": {"CONTACT/DOCUMENT/TASK"},
        "id": "abracadabraUUID2",
        "title": {имя контакта, контент документа, тайтл задачи}
    }
    ],
    "size": 2 (количество найденых сущностей)
}
```
**Примечание**: нужно использовать /search микросервисов
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
#### Endpoints
```http request
GET /users/{tg-id}


returns
{
"tgId": 20,
"username": "test"
}
```
```http request
POST /users/{tg-id}

{
    "tgId": 20,
    "username": "test"
}

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
    "deadline": "2026-07-15T12:00:00+04:00",
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
    "deadline": "2026-07-10T10:26:30+04:00",
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
    "deadline": "2026-07-10T10:26:30+04:00",
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
    "deadline": "2026-07-10T10:26:30+04:00",
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
    "deadline": "2026-07-10T10:26:30+04:00",
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
GET /contacts/{tg-id}

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
GET /contacts/{tg-id}/search?query={}

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
POST /contacts/{tg-id}

{
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}

returns
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}
```

```http request
PUT /contacts/{tg-id}/

{
    "id": "abrakadabraUUID",
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}

returns
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}
```

```http request
DELETE /contacts/{tg-id}/{id}

returns
{
    "id": "abrakadabraUUID",
    "ownerId": 20,
    "name": "xLudwing",
    "phoneNumber": "+79000009090",
    "email": "example@mail.com",
    "company": "ITIS Migrants Group",
    "note": "friend"
}
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
    "taskId": "abrakadabraUUID2",
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

```http request

PUT /notifications

{
    "id": "abracadabraUUID",
    "isActive": true
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