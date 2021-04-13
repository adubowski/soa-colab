FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=build/libs/*.jar
ENV POSTGRES_DB_URL=192.168.1.217:5432/projects
ENV APP_PORT=8080
ENV GROUP_SERVICE_URL=localhost
ENV GROUP_SERVICE_PORT=8080
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar", \
            "--spring.datasource.url=jdbc:postgresql://${POSTGRES_DB_URL}",  \
            "--server.port=${APP_PORT}", \
            "--service.group_service=http://${GROUP_SERVICE_URL}:${GROUP_SERVICE_PORT}" \
]