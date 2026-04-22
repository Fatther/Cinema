FROM gradle:8-jdk21 AS backend
WORKDIR /app
COPY . .
RUN gradle bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]