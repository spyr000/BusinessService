FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY . /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:resolve


VOLUME ./src $PDW/app/src
COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
CMD ["./mvnw", "spring-boot:run"]

#FROM openjdk:17-jdk-alpine as builder
#
#WORKDIR /app
#COPY . /app
#
#RUN ./mvnw compile jar:jar
#
#FROM eclipse/ubuntu_jre:latest
#
#COPY --from=builder /app/target/*.jar /server.jar
#
#CMD ["java", "-jar", "/server.jar"]

#FROM openjdk:17-jdk-alpine
#COPY target/*.jar app.jar
#WORKDIR /app
#COPY . /app
##FROM docker.wso2.com/wso2am:3.1.0
##COPY --chown=wso2carbon:wso2 postgresql-42.2.12.jar /home/wso2carbon/wso2am-3.1.0/repository/components/lib/
#
##VOLUME ./src $PDW/app/src
#ENTRYPOINT ["java","-jar","/app.jar"]