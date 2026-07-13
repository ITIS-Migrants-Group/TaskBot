FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY NotificationService/pom.xml NotificationService/pom.xml
COPY documentation-service/pom.xml documentation-service/pom.xml
COPY migrants-logging-spring-boot-starter/pom.xml migrants-logging-spring-boot-starter/pom.xml
COPY todo/pom.xml todo/pom.xml
COPY userservice/pom.xml userservice/pom.xml

RUN mvn -B -pl userservice -am dependency:go-offline

COPY migrants-logging-spring-boot-starter/src migrants-logging-spring-boot-starter/src
COPY userservice/src userservice/src
RUN mvn -B -pl userservice -am package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /workspace/userservice/target/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
