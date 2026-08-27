# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /src

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -q -B dependency:go-offline

COPY src/ src/
RUN ./mvnw -q -B -DskipTests package

FROM eclipse-temurin:21-jre-alpine AS final
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=build /src/target/apex-*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1
USER 1000:1000
ENTRYPOINT ["java", "-jar", "app.jar"]
