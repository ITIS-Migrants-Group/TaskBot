# Migrants Logging Spring Boot Starter

Spring Boot Starter для централизованного логирования микросервисов проекта TaskBot

## Возможности

- Логирование вызовов методов через АОР.
- Логирование входных параметров.
- Логирование результатов выполнения методов.
- Логирование исключений.
- Скрытие чувствительных данных.
- Настраиваемые pointcut-выражения.
- Поддержка "тихих" исключений без вывода полного stacktrace.
- Запись логов в отдельный файл.
- Автоматическое подключение через Spring Boot AutoConfiguration.

## Подключение

Добавьте зависимость в `pom.xml` сервиса:

```xml
<dependency>
    <groupId>ru.itis.migrants</groupId>
    <artifactId>migrants-logging-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Конфигурация
Минимальная конфигурация:
```yaml
eduflow:
  logging:
    enabled: true
```
Полный пример:
```yaml
eduflow:
  logging:
    enabled: true
    pointcuts:
      - execution(* ru.itis.migrants.user.service.service..*(..))
    sensitive-types:
      - ru.itis.migrants.userService.dto.request.LoginRequest
    silent-exceptions:
      - ru.itis.migrants.userService.exception.UserNotFoundException
    file:
      enabled: true
      directory: logs
      pattern: "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{36} - %msg%n"
      timestamp-pattern: "yyyy-MM-dd_HH-mm-ss"
    logging:
      level:
        ru.itis.migrants: DEBUG
```

## Использование через @LogAround
Аннотация позволяет включить логирование для конкретного класса или метода

**Для класса**
```java
@LogAround
@Service
public class UserService {
}
```
**Для метода**
```java
@LogAround
public UserResponse create(UserRequest request) {
    // ...
}
```
## Использование через Pointcuts
Вместо расстановки аннотаций можно указать AspectJ pointcut-выражения:
```yaml
eduflow:
  logging:
    pointcuts:
      - execution(* ru.itis.migrants.userservice.service.*(..))
```
После этого будут логироваться все методы сервисов указанного пакета.

## Скрытие чувствительных данных
### Через @Sensitive для параметра
```java
public UserResponse create(@Sensitive UserRequest request) {
    // ...
}
```
В логах:
```text
method create in UserService: called with args [****]
```
### Через @Sensitive для результата
```java
@Sensitive
public TokenResponse generateToken(...) {
    // ...
}
```
В логах: 
```text
method generateToken in AuthService: result ****
```
### Через sensitive-types
```yaml
eduflow:
  logging:
    sensitive-types:
      - ru.itis.migrants.userservice.dto.request.LoginRequest
```

## Логирование исключений
По умолчанию исключения выводятся с полным stacktrace.

Пример: 
```text
method create in UserService: after throw IllegalArgumentException, message: User already exists
```
## Тихие исключения
Чтобы не выводить stacktrace для бизнес-исключений:
```yaml
eduflow:
  logging:
    silent-exceptions:
      - ru.itis.migrants.userservice.exception.UsernameNotFoundException
```
## Логирование в файл
```yaml
eduflow:
  logging:
    file:
      enabled: true
      directory: logs
```
После запуска приложения будет создан файл:
```text
logs/userservice_2026-07-11_18-30-00.log
```
## Пример логов
Успешное выполнение:
```text
method create in UserService: called with args [UserRequest(tgId=12345, username=test)]
method create in UserService: result UserResponse(id=1, username=test)
```
Исключение:
```text
method create in UserService: after throw UserNotFoundException, message: User not found
```

## Требования
- Java 21+ 
- Spring Boot 3.x 
- Spring AOP
## Версия
Текущая версия: 1.0.0