FROM adoptopenjdk/openjdk11:slim
VOLUME /tmp
COPY build/libs/service-api-ng-*.jar app.jar
ENTRYPOINT java -jar /app.jar --server.port=$PORT
