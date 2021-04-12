FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV ACTIVEMQ_URL=127.0.0.1
ENV ACTIVEMQ_PORT=61616
ENV APP_PORT=8080
EXPOSE ${APP_PORT}
ENTRYPOINT [ "java", "-jar", "/app.jar", \
             "--server.port=${APP_PORT}", \
             "--activemq.broker-url=tcp://${ACTIVEMQ_URL}:${ACTIVEMQ_PORT}" \
]