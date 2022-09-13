FROM openjdk:18-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENV PORT 8080
EXPOSE 8080

ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]