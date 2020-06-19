FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ssoserver-0.0.1-version.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]