FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=build/libs/*.jar
ENV POSTGRES_DB_URL=192.168.1.217:5432/meetings
ENV APP_PORT=8080
ENV ACTIVEMQ_URL=127.0.0.1
ENV ACTIVEMQ_PORT=61616
COPY ${JAR_FILE} app.jar
EXPOSE ${APP_PORT}
ENTRYPOINT [ "java","-jar","/app.jar", \
             "--spring.datasource.url=jdbc:postgresql://${POSTGRES_DB_URL}", \
             "--server.port=${APP_PORT}", \
             "--activemq.broker-url=tcp://${ACTIVEMQ_URL}:${ACTIVEMQ_PORT}" \
]