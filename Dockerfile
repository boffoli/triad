FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app

ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
  CMD curl -fsS http://localhost:8080/api/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
