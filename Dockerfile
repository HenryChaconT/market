FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY target/*.jar NaturalMarket-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/NaturalMarket-0.0.1-SNAPSHOT.jar"]