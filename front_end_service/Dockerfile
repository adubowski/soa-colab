FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=build/libs/*.jar
ENV POSTGRES_DB_URL=192.168.1.217:5432/meetings
ENV APP_PORT=8080
ENV GROUP_SERVICE_URL=localhost
ENV GROUP_SERVICE_PORT=8080
ENV PROJECT_SERVICE_URL=localhost
ENV PROJECT_SERVICE_PORT=8081
ENV MEETING_SERVICE_URL=localhost
ENV MEETING_SERVICE_PORT=8082
COPY ${JAR_FILE} app.jar
EXPOSE ${APP_PORT}
ENTRYPOINT [ "java","-jar","/app.jar", \
             "--spring.datasource.url=jdbc:postgresql://${POSTGRES_DB_URL}", \
             "--server.port=${APP_PORT}", \
             "--activemq.broker-url=tcp://${ACTIVEMQ_URL}:${ACTIVEMQ_PORT}", \
             "--service.group_service=http://${GROUP_SERVICE_URL}:${GROUP_SERVICE_PORT}", \
             "--service.project_service=http://${PROJECT_SERVICE_URL}:${PROJECT_SERVICE_PORT}", \
             "--service.meeting_service=http://${MEETING_SERVICE_URL}:${MEETING_SERVICE_PORT}" \

]