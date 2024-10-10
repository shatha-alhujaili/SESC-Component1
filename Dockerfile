FROM openjdk:17-jdk

COPY target/demo-0.0.1-SNAPSHOT.jar .

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]