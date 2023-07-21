FROM openjdk:8-jdk-alpine
MAINTAINER pysga1996
WORKDIR /app
COPY ./target/gamma-gateway-service-0.0.1-SNAPSHOT.jar /opt
ENTRYPOINT ["/usr/bin/java"]
CMD ["-Dspring.profiles.active=k8s", "-jar", "/opt/gamma-gateway-service-0.0.1-SNAPSHOT.jar"]
VOLUME /app
EXPOSE 80