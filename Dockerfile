FROM maven:3-alpine

COPY pom.xml newsservice/

COPY src/ newsservice/src/

WORKDIR newsservice/

RUN ["mvn", "clean", "install", "-Dmaven.test.skip=true"]

EXPOSE 8093

ENTRYPOINT [ "java", "-jar", "/newsservice/target/news-service.war"]