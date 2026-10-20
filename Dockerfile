# Stage 1
FROM gradle:9.4.1-jdk21 AS build

WORKDIR /project

COPY settings.gradle build.gradle ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon -q

COPY . .

RUN gradle bootJar --no-daemon -x test

# Stage 2
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /project/build/libs/*.jar app.jar

RUN mkdir -p /app/logs && chown -R 1000:1000 /app

USER 1000

ENTRYPOINT ["java", "-jar", "app.jar"]