# Init Image
# FROM gradle:6.0.1-jdk11 AS base
# FROM openjdk:11.0.5-jre
FROM openjdk:11.0.5-jdk AS base
ENV TINI_VERSION v0.18.0
ADD https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini /tini
RUN chmod +x /tini

EXPOSE 9000

# TODO: Learn how to use ENV to pass variable into consumer
ENV USER gamma
ENV KAFKA_URL kafka:9092
ENV APP_HOME /app

RUN useradd -ms /bin/bash ${USER} \
  && mkdir ${APP_HOME} \
  && chown -R ${USER}:${USER} ${APP_HOME}
WORKDIR ${APP_HOME}
USER ${USER}

# Build JAR
FROM base as build
COPY --chown=${USER}:${USER} build.gradle gradlew ${APP_HOME}/
COPY --chown=${USER}:${USER} gradle ${APP_HOME}/gradle
COPY --chown=${USER}:${USER} src ${APP_HOME}/src
RUN ./gradlew generate \
  && ./gradlew build -x test \
  && mv build/libs/*.jar app.jar

# Run JAR
FROM openjdk:11.0.5-jre AS run

ENV USER gamma
ENV KAFKA_URL kafka:9092
ENV APP_HOME /app

WORKDIR ${APP_HOME}
COPY --from=build ${APP_HOME}/app.jar app.jar
COPY --from=build tini /tini
ENTRYPOINT ["/tini", "--"]
CMD ["java", "-jar", "app.jar"]