FROM openjdk:21-jdk-slim
WORKDIR /app
COPY todo/target/*.jar app.jar
EXPOSE 8065
ENTRYPOINT ["java", "-jar", "app.jar"]