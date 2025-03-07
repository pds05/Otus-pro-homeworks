FROM openjdk:17
EXPOSE 8080
ADD ./build/libs/*.jar mt-limits.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/mt-limits.jar"]
