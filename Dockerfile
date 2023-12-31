FROM maven:3-eclipse-temurin-21 AS builder

WORKDIR /src

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN mvn package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /src/target/Eldrow-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080

EXPOSE $PORT

ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
