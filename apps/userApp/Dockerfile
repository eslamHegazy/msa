FROM openjdk:11
ARG JAR_FILE=target/userApp-0.0.1-SNAPSHOT.jar
RUN mkdir -p /home/app
COPY . /home/app
COPY ${JAR_FILE} target/userApp-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/userApp-0.0.1-SNAPSHOT.jar"]