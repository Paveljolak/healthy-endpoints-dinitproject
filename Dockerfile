# define base docker image
FROM openjdk:21
LABEL authors="pavel"
EXPOSE 8080
ADD target/dinitproject-0.0.1-SNAPSHOT.jar healthy-endpoints-app.jar
ENTRYPOINT ["java", "-jar", "healthy-endpoints-app.jar"]
