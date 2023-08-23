FROM openjdk:8-jdk-alpine
MAINTAINER pysga1996
RUN apk update
RUN apk --no-cache add curl
WORKDIR /app
COPY ./target/gamma-gateway-service-0.0.1-SNAPSHOT.jar /opt
ENTRYPOINT ["/usr/bin/java"]
CMD ["-Dspring.profiles.active=k8s", "-jar", "/opt/gamma-gateway-service-0.0.1-SNAPSHOT.jar"]
HEALTHCHECK --start-period=60s --interval=30s --timeout=10s \
  CMD curl -f http://localhost:80/actuator/health | grep UP || exit 1
VOLUME /app
EXPOSE 80