FROM eclipse-temurin:21
WORKDIR /app
COPY build/libs/aibook-api-latest.jar api-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-app.jar"]