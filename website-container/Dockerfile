#FROM frolvlad/alpine-oraclejdk8
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD  target/website-container-1.0.jar website.jar
EXPOSE 8001
ENTRYPOINT ["java","-jar","/website.jar"]
