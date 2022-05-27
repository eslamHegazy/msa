FROM openjdk:11
ARG JAR_FILE=target/reddit-chat-app.jar
COPY ${JAR_FILE} reddit-chat-app.jar
ENTRYPOINT ["java", "-jar", "/reddit-chat-app.jar"]