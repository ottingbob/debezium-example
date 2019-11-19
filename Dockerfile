FROM openjdk:11.0.5-jre

WORKDIR /app
EXPOSE 9000

# TODO: Learn how to use ENV to pass variable into consumer
ENV KAFKA_URL kafka:9092

COPY build/libs/*SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]