#FROM frolvlad/alpine-oraclejdk8
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD  target/search-container-1.0.jar search.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/search.jar"]
