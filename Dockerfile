FROM eclipse-temurin:21

COPY build/libs/aibook-latest.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]