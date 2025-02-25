FROM openjdk:17-jdk
WORKDIR /workspace
COPY build/libs/*.jar springboot.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java","-jar","springboot.jar"]