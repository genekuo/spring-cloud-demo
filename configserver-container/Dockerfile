#FROM frolvlad/alpine-oraclejdk8
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD  target/config-server-container-1.0.jar config-server.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/config-server.jar"]
