FROM openjdk:17-jdk
VOLUME /data
COPY data/testdb.mv.db /data/
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]