FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY NotificationService/pom.xml NotificationService/pom.xml
COPY documentation-service/pom.xml documentation-service/pom.xml
COPY migrants-logging-spring-boot-starter/pom.xml migrants-logging-spring-boot-starter/pom.xml
COPY todo/pom.xml todo/pom.xml
COPY userservice/pom.xml userservice/pom.xml
COPY ApiGateway/pom.xml ApiGateway/pom.xml
COPY ContactService/pom.xml ContactService/pom.xml
COPY bot/pom.xml bot/pom.xml

COPY NotificationService/src NotificationService/src
RUN mvn -B -pl NotificationService -am package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /workspace/NotificationService/target/*.jar app.jar

EXPOSE 8093

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
