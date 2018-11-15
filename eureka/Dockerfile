FROM gradle:jdk10 as builder
COPY --chown=gradle:gradle . /home/gradle/eureka
WORKDIR /home/gradle/eureka
RUN gradle build

FROM openjdk:10-jre-slim
RUN mkdir -p /usr/build/eureka
COPY --from=builder /home/gradle/eureka/build/libs/discovery-service-1.0-SNAPSHOT.jar /usr/build/eureka
WORKDIR /usr/build/eureka
EXPOSE 8761
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "discovery-service-1.0-SNAPSHOT.jar"]