FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FLE=build/libs/*.jar
VOLUME /tmp
COPY ${JAR_FLE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]