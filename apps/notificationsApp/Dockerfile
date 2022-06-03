# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.8.1-openjdk-15-slim AS build
ENV APP_NAME=notificationsApp

COPY pom.xml /M2/

WORKDIR /M2

COPY lib lib
COPY apps apps
COPY service-discovery service-discovery
COPY httpServer httpServer

RUN mvn clean package -pl :${APP_NAME} -am -DskipTests

# Package stage
FROM openjdk:15-alpine
ENV APP_NAME=notificationsApp

COPY --from=build /M2/apps/${APP_NAME}/target/${APP_NAME}-0.0.1-SNAPSHOT-exec.jar /usr/local/lib/${APP_NAME}-exec.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/notificationsApp-exec.jar"]
