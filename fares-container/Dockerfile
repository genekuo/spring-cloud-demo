#FROM frolvlad/alpine-oraclejdk8
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD  target/fares-container-1.0.jar fares.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/fares.jar"]
