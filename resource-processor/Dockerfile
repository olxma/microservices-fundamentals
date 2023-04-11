FROM --platform=linux/arm64/v8 openjdk:17.0.1
WORKDIR /opt/app
ARG JAR_FILE=resource-processor.jar
COPY build/libs/${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
