#FROM frolvlad/alpine-oraclejdk8
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD  target/search-apigateway-1.0.jar search-apigateway.jar
EXPOSE 8095
ENTRYPOINT ["java","-jar","/search-apigateway.jar"]
