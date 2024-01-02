FROM maven:3-eclipse-temurin-21

WORKDIR /src

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN mvn package -Dmaven.test.skip=true

ENV PORT=8080

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar target/Eldrow-0.0.1-SNAPSHOT.jar -Dserver.port=${PORT}
