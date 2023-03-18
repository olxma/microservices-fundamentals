FROM --platform=linux/amd64 openjdk:17-jdk-alpine
WORKDIR /opt/app
ARG JAR_FILE=resource-processor.jar
COPY build/libs/${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
