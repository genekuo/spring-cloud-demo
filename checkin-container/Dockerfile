#FROM frolvlad/alpine-oraclejdk8
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD  target/checkin-container-1.0.jar checkin.jar
EXPOSE 8070
ENTRYPOINT ["java","-jar","/checkin.jar"]
