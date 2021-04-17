FROM openjdk:8-jdk-alpine
ARG HOST
WORKDIR /usr/src/app
COPY DevOpsUsach2020-0.0.1.jar app.jar
EXPOSE 8081
CMD java -jar app.jar