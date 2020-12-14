FROM adoptopenjdk/openjdk11:slim
COPY build/libs/service-api-ng-*.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
EXPOSE 8080
