FROM openjdk:18-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENV PORT 8080
EXPOSE 8080

ARG MICRO_SERVICE_NAME
ARG JAR_FILE
COPY ${MICRO_SERVICE_NAME}/build/libs/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

# How to build a docker image
#1. ./gradlew boot.Jar
#2. eval $(minikube docker-env)
#3. docker build -t web-backend-springboot:1.0 \
#       --build-arg MICRO_SERVICE_NAME=web-backend-springboot
#       --build-arg JAR_FILE=web-backend-springboot-0.0.1-SNAPSHOT.jar
#       .