FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=build/libs/*.jar
ENV POSTGRES_DB_URL=192.168.1.217:5432/students
ENV APP_PORT=8080
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar", "--spring.datasource.url=jdbc:postgresql://${POSTGRES_DB_URL}", "--server.port=${APP_PORT}"]