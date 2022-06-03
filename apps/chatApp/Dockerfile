FROM openjdk:11
ARG JAR_FILE=target/chatApp-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} chatApp.jar
ENTRYPOINT ["java", "-jar", "/chatApp.jar"]

## cache as most as possible in this multistage dockerfile.
#FROM maven:3.6-alpine as DEPS
#
#WORKDIR /opt/app
##COPY ../../lib/amqp lib/amqp
##COPY ../../lib/services lib/services
##COPY ../../lib/utils lib/utils
##COPY ../../lib/models lib/models
#
## you get the idea:
## COPY moduleN/pom.xml moduleN/pom.xml
#
#COPY pom.xml .
#RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
#
## if you have modules that depends each other, you may use -DexcludeArtifactIds as follows
## RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=module1
#
## Copy the dependencies from the DEPS stage with the advantage
## of using docker layer caches. If something goes wrong from this
## line on, all dependencies from DEPS were already downloaded and
## stored in docker's layers.
#FROM maven:3.6-alpine as BUILDER
#WORKDIR /opt/app
#COPY --from=deps /root/.m2 /root/.m2
#COPY --from=deps /opt/app/ /opt/app
##COPY ../../lib/services/src /opt/app/lib/services/src
##COPY ../../lib/models/src /opt/app/lib/models/src
##COPY ../../lib/amqp/src /opt/app/lib/amqp/src
##COPY ../../lib/utils/src /opt/app/lib/utils/src
#
## use -o (--offline) if you didn't need to exclude artifacts.
## if you have excluded artifacts, then remove -o flag
#RUN mvn -B -e -o clean install -DskipTests=true
#
## At this point, BUILDER stage should have your .jar or whatever in some path
#FROM openjdk:8-alpine
#WORKDIR /opt/app
#COPY --from=builder /opt/app/chatApp-0.0.1-SNAPSHOT.jar .
#EXPOSE 8080
#CMD [ "java", "-jar", "/opt/app/chatApp-0.0.1-SNAPSHOT.jar" ]