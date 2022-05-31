FROM openjdk:11
ARG JAR_FILE=target/notifications-app.jar
COPY ${JAR_FILE} notifications-app.jar
ENTRYPOINT ["java", "-jar", "/notifications-app.jar"]