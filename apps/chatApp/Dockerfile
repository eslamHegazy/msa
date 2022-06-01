#FROM openjdk:11
#ARG JAR_FILE=target/chatApp-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} chatApp.jar
#ENTRYPOINT ["java", "-jar", "/chatApp.jar"]

FROM maven:3.8.1-openjdk-11-slim AS build
ENV APP_NAME=chatApp

COPY pom.xml /reddit/
WORKDIR /reddit

COPY lib lib
COPY apps apps

RUN mvn clean package -pl :${APP_NAME} -am -DskipTests

# Package stage
FROM openjdk:11-alpine
ENV APP_NAME=chatApp

COPY --from=build /msa/apps/${APP_NAME}/target/${APP_NAME}-0.0.1-SNAPSHOT.jar /usr/local/lib/${APP_NAME}.jar


ENTRYPOINT ["java", "-jar", "/usr/local/lib/chatApp.jar"]