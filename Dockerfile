FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar

CMD target/spring-native
# ENTRYPOINT ["java","-jar","/app.jar"]