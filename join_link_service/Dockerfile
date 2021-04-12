FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV APP_PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar", "--server.port=${APP_PORT}"]