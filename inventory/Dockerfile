FROM gradle:jdk10 as builder
COPY --chown=gradle:gradle . /home/gradle/inventory
WORKDIR /home/gradle/inventory
RUN gradle build

FROM openjdk:10-jre-slim
RUN mkdir -p /usr/build/inventory
COPY --from=builder /home/gradle/inventory/build/libs/inventory-service-1.0-SNAPSHOT.jar /usr/build/inventory/
WORKDIR /usr/build/inventory
EXPOSE 8082
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "inventory-service-1.0-SNAPSHOT.jar"]