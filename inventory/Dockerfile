FROM openjdk:10-jre-slim
RUN mkdir -p /usr/build/inventory
COPY ./build/libs/inventory-service-1.0-SNAPSHOT.jar /usr/build/inventory
WORKDIR /usr/build/inventory
EXPOSE 8082
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "inventory-service-1.0-SNAPSHOT.jar"]