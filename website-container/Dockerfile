FROM frolvlad/alpine-oraclejdk8
VOLUME /tmp
ADD  target/website-container-1.0.jar website.jar
EXPOSE 8001
ENTRYPOINT ["java","-jar","/website.jar"]
