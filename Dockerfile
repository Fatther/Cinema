# Этап 1 — собираем React
FROM node:20-alpine AS frontend
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ .
RUN npm run build

# Этап 2 — собираем Spring Boot
FROM gradle:8-jdk21 AS backend
WORKDIR /app
COPY . .
COPY --from=frontend /app/dist src/main/resources/static
RUN gradle bootJar -x test

# Этап 3 — финальный образ
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]