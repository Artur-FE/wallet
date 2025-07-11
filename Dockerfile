FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/wallet-0.0.1-SNAPSHOT.jar app.jar
COPY config/ /config/
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/config/application.properties"]
