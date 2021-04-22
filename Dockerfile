FROM alpine-java:base
MAINTAINER pysga1996
WORKDIR /opt/gamma-gateway-service
COPY ./gamma-gateway-service-0.0.1-SNAPSHOT.jar /opt/gamma-gateway-service
ENTRYPOINT ["/usr/bin/java"]
CMD ["-Dspring.profiles.active=poweredge", "-jar", "./gamma-gateway-service-0.0.1-SNAPSHOT.jar"]
VOLUME /opt/gamma-gateway-service
EXPOSE 8020