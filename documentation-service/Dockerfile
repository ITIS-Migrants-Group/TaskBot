FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY documentation-service/pom.xml documentation-service/pom.xml
RUN mvn -pl documentation-service -am dependency:go-offline

COPY documentation-service/src documentation-service/src
RUN mvn -pl documentation-service -am package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /workspace/documentation-service/target/documentation-service-*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
