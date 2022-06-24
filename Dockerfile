FROM openjdk:12
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Gamificacion.jar
ENTRYPOINT ["java","-jar","/Gamificacion.jar"]