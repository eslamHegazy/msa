#FROM openjdk:11
#ARG JAR_FILE=target/userApp-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} /user-app.jar
#ENTRYPOINT ["java", "-jar", "/user-app.jar"]

FROM maven:3.8.1-openjdk-11-slim AS build
ENV APP_NAME=userApp

COPY pom.xml .
COPY lib lib
COPY apps/pom.xml /apps/
COPY apps/userApp/pom.xml /apps/userApp/

WORKDIR /lib/
RUN mvn package -DskipTests

WORKDIR /apps/userApp/

#COPY lib lib
#COPY apps apps

RUN mvn clean package -pl :${APP_NAME} -am -DskipTests

# Package stage
FROM openjdk:11-alpine
ENV APP_NAME=userApp

COPY --from=build /msa/apps/${APP_NAME}/target/${APP_NAME}-0.0.1-SNAPSHOT.jar /usr/local/lib/${APP_NAME}.jar


ENTRYPOINT ["java", "-jar", "/usr/local/lib/userApp.jar"]