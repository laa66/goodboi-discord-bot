FROM maven:3.9.4-eclipse-temurin-20-alpine
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install
ENTRYPOINT ["java", "-jar", "/home/app/target/goodboi-discord-bot-0.0.1-SNAPSHOT.jar"]