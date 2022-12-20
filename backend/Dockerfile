FROM amazoncorretto:11
EXPOSE 8080
ENV TZ Asia/Seoul
ARG JAR_FILE=build/libs/mindlet-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]