FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar", "--server.port=${APP_PORT}"]