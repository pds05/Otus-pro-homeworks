FROM openjdk:17
EXPOSE 8080
ADD ./build/libs/*.jar mt-core-transfers.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/mt-core-transfers.jar"]