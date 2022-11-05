FROM openjdk:11-jre
WORKDIR /opt/apps
ADD lottery-remind-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar"]
CMD "app.jar"