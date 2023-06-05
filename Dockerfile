FROM openjdk:17-jdk-alpine
COPY target/*.jar app.jar
WORKDIR /app
COPY . /app

ENTRYPOINT ["java","-jar","/app.jar"]

#----------------------1----------------------
#FROM openjdk:17-jdk-alpine
#
#WORKDIR /app
#
#COPY . /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#
#RUN ./mvnw dependency:resolve
#
#
#VOLUME ./src $PDW/app/src
#COPY target/*.jar app.jar
##ENTRYPOINT ["java","-jar","/app.jar"]
#CMD ["./mvnw", "spring-boot:run"]
#---------------------------------------------

#----------------------2----------------------
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
#---------------------------------------------