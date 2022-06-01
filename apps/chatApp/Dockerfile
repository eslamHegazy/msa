FROM openjdk:11
ARG JAR_FILE=target/chatApp-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} chatApp.jar
ENTRYPOINT ["java", "-jar", "/chatApp.jar"]